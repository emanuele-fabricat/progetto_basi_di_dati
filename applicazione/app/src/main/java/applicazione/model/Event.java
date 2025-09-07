package applicazione.model;

import java.sql.Connection;
import java.util.Optional;

import applicazione.dao.DAODataConfig;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public record Event(String eventId, int numPartecipanti, String inizio, String fine, String type,
        Optional<String> nome, Optional<String> presentazione, Optional<Integer> maxPartecipanti) {
    private static final String RIGHT_ID_QUERY = "SELECT id_evento FROM EVENTO WHERE id_evento = ?";
    private static final String GET_VISIBILITY_QUERY = "SELECT visibilità FROM EVENTO WHERE id_evento = ?";
    private static final String REMAIN_PLACES_QUARY = "SELECT id_evento FROM EVENTO WHERE num_partecipanti < max_partecipanti and id_evento = ?";
    private static final String BEFORE_DATE_QUERY = "SELECT * FROM EVENTO " + "WHERE data_ora_inizio > ? AND id_evento = ?";
    private final static Connection connection = DAOUtils.localMySQLConnection(DAODataConfig.DATABASE, DAODataConfig.USERNAME,
            DAODataConfig.PASSWORD);
    public static final int ADMIN = 0;
    public static final int CLIENT = 1;

    public static boolean existId(final String id) {
        try (
                var stm = DAOUtils.prepare(connection, RIGHT_ID_QUERY, id);
                var rS = stm.executeQuery();) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public static boolean stillAvailable (final String eventId) {
        try (
            var stm = DAOUtils.prepare(connection, REMAIN_PLACES_QUARY, eventId);
            var rS = stm.executeQuery();
        ) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public static String getType (final String eventId) {
        try (
            var stm = DAOUtils.prepare(connection, GET_VISIBILITY_QUERY, eventId);
            var rS = stm.executeQuery();
        ) {
            rS.next();
            return rS.getString("visibilità");
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

    public static boolean isBefore (final String eventId, final String date) {
        try (
            var stm = DAOUtils.prepare(connection, BEFORE_DATE_QUERY, date, eventId);
            var rS = stm.executeQuery();
        ) {
            return rS.next();
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

}
