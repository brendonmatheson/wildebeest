package co.zd.wb.model.mysql;

import co.zd.wb.model.database.Extensions;
import co.zd.wb.model.base.BaseResource;
import co.zd.wb.model.database.DatabaseHelper;
import co.zd.wb.model.IndeterminateStateException;
import co.zd.wb.model.ModelExtensions;
import co.zd.wb.model.Resource;
import co.zd.wb.model.Instance;
import co.zd.wb.model.FaultException;
import co.zd.wb.model.State;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class MySqlDatabaseResource extends BaseResource implements Resource
{
	public MySqlDatabaseResource(
		UUID resourceId,
		String name)
	{
		super(resourceId, name);
	}
	
	//
	// Behaviour
	//

	@Override public State currentState(Instance instance) throws IndeterminateStateException
	{
		if (instance == null) { throw new IllegalArgumentException("instance cannot be null"); }
		MySqlDatabaseInstance db = ModelExtensions.As(instance, MySqlDatabaseInstance.class);
		if (db == null) { throw new IllegalArgumentException("instance must be a DatabaseInstance"); }

		UUID declaredStateId = null;
		
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		if (MySqlDatabaseHelper.schemaExists(db, db.getSchemaName()))
		{
			String stateTableName = Extensions.getStateTableName(db);
			
			try
			{
				conn = db.getAppDataSource().getConnection();
				ps = conn.prepareStatement("SELECT StateId FROM " + stateTableName + ";");
				rs = ps.executeQuery();

				if (rs.next())
				{
					declaredStateId = UUID.fromString(rs.getString(1));

					if (rs.next())
					{
						throw new IndeterminateStateException(String.format(
							"Multiple rows found in the state tracking table \"%s\"",
							stateTableName));
					}
				}
				else
				{
					throw new IndeterminateStateException(String.format(
						"The state tracking table \"%s\" was not found in the target schema",
						stateTableName));
				}
			}
			catch(SQLException e)
			{
				throw new FaultException(e);
			}
			finally
			{
				try
				{
					DatabaseHelper.release(rs);
					DatabaseHelper.release(ps);
					DatabaseHelper.release(conn);
				}
				catch(SQLException e)
				{
					throw new FaultException(e);
				}
			}
		}
		
		// If we found a declared state, check that the state is actually defined
		State result = null;
		if (declaredStateId != null)
		{
			for (State check : this.getStates())
			{
				if (declaredStateId.equals(check.getStateId()))
				{
					result = check;
					break;
				}
			}

			// If the declared state ID is not known, throw
			if (result == null)
			{
				throw new IndeterminateStateException(String.format(
					"The resource is declared to be in state %s, but this state is not defined for this resource",
					declaredStateId.toString()));
			}
		}
		
		return result;
	}
}