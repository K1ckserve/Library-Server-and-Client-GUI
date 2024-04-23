package network;


public interface CatalogUpdateListener{
    void onCatalogUpdate(Catalog catalog);
    void onClientCatalogUpdate(Catalog catalog);
}
