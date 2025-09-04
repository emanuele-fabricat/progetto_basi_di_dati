package applicazione.model;

import java.sql.Connection;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public record Admin() {
    private static final String RIGHT_ID_QUERY = "SELECT id_admin FROM ADMIN WHERE id_admin = ?";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);



    public static boolean existId (final String id) {
        try (
            var stm = DAOUtils.prepare(connection, RIGHT_ID_QUERY, id);
            var rS = stm.executeQuery();
        ) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

}
