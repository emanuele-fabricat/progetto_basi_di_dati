package applicazione.model;

import java.sql.Connection;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public record Client() {
    private static final String RIGHT_ID_QUERY = "SELECT id_utente FROM CLIENTE WHERE id_utente = ?";
    private static final String GET_ID_QUERY = "SELECT id_utente FROM CLIENTE WHERE mail = ?";
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

    public static String getId (final String mail) {
        try (
            var stm = DAOUtils.prepare(connection, GET_ID_QUERY, mail);
            var rS = stm.executeQuery();
        ) {
            rS.next();
            return rS.getString("id_utente");
        } catch (Exception e) {
            throw new DAOException(e);
        }

    }

}
