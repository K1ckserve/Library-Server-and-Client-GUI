package network;
import common.*;


public interface CatalogUpdateListener{
    void onCatalogUpdate(Catalog catalog);
    void onClientCatalogUpdate(Catalog catalog);
}
