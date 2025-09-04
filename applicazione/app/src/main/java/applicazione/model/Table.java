package applicazione.model;

import java.sql.Connection;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public record Table () {
    private static final String RIGHT_ID_QUERY = "SELECT numero FROM TAVOLO WHERE numero = ?";
    private static final String OCCUPATION_ID_QUERY = "SELECT T.numero " + "FROM TAVOLO T, UTILIZZARE U, EVENTO E "
            + "WHERE U.numero = ? " + "AND U.id_evento = E.id_evento " + "AND E.data_ora_inizio < ? "
            + "AND E.data_ora_fine > ?";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAOData.DATABASE, DAOData.USERNAME,
            DAOData.PASSWORD);

    public static boolean existId(final String id) {
        try (
                var stm = DAOUtils.prepare(connection, RIGHT_ID_QUERY, id);
                var rS = stm.executeQuery();) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public static boolean isOccupied(final String id, final String start, final String end) {
        try (
                var stm = DAOUtils.prepare(connection, OCCUPATION_ID_QUERY, id, end, start);
                var rS = stm.executeQuery();) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

}
