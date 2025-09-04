package applicazione.model;

import java.sql.Connection;
import java.util.Optional;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public record Event(String userId, String eventId, int numPartecipanti, String inizio, String fine, String type,
        Optional<String> nome, Optional<String> presentazione, Optional<Integer> maxPartecipanti) {
    private static final String RIGHT_ID_QUERY = "SELECT id_evento FROM EVENTO WHERE id_evento = ?";
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

}
