package applicazione.model;

import java.sql.Connection;

import applicazione.dao.DAOData;
import applicazione.dao.DAOException;
import applicazione.dao.DAOUtils;

public record Product() {
    private static final String RIGHT_ID_QUERY = "SELECT id_prodotto FROM PRODOTTO WHERE id_prodotto = ?";
    private static final String SEARCH_NAME_QUERY = "SELECT nome_prodotto FROM PRODOTTO WHERE id_prodotto = ?";
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

    public static String getProductName (final String productId) {
        try (
            var stm = DAOUtils.prepare(connection, SEARCH_NAME_QUERY, productId);
            var rS = stm.executeQuery();
        ) {
            rS.next();
            return rS.getString("nome_prodotto");
        } catch (Exception e) {
            throw new DAOException(e);
        }
    }

}
