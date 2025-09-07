package applicazione.model;

import java.sql.Connection;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public record Order() {
    private static final String RIGHT_ID_QUERY = "SELECT id_ordine FROM ORDINE WHERE id_ordine = ?";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);



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
