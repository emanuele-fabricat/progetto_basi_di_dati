package applicazione.model;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public record Table(int numero, int capienza) {
    private static final String RIGHT_ID_QUERY = "SELECT numero FROM TAVOLO WHERE numero = ?";
    private static final String OCCUPATION_ID_QUERY = "SELECT T.numero " + "FROM TAVOLO T, UTILIZZARE U, EVENTO E "
            + "WHERE U.numero = ? " + "AND U.id_evento = E.id_evento " + "AND E.data_ora_inizio < ? "
            + "AND E.data_ora_fine > ?";
    private static final String GET_ALL_TABLE = "SELECT * FROM TAVOLO";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);

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

    public static Map<Integer, Integer> getAllTable() {
        Map<Integer, Integer> tables = new HashMap<>();
        try (
                var stm = DAOUtils.prepare(connection, GET_ALL_TABLE);
                var rS = stm.executeQuery();) {
            while (rS.next()) {
                tables.put(rS.getInt("numero"), rS.getInt("capienza"));
            }
            return tables;
        } catch (Exception e) {
            throw new DAOException(e);
        }

    }

}
