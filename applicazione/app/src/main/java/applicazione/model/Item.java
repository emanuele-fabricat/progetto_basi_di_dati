package applicazione.model;

import java.sql.Connection;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public record Item() {
    private static final String RIGHT_ID_QUERY = "SELECT id_articolo FROM ARTICOLO WHERE id_articolo = ?";
    private static final String GET_QTA_QUERY = "SELECT disponibilità FROM ARTICOLO WHERE id_articolo = ?";
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
    public static int getQta (final String id) {
        try (
            var stm = DAOUtils.prepare(connection, GET_QTA_QUERY, id);
            var rS = stm.executeQuery();
        ) {
            rS.next();
            return rS.getInt("disponibilità");
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

}
