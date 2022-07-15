package com.kgaft.securemessengerappandroid.Database;

import java.util.List;

public interface TableInterface {
    List<TableColumn> getAllValues();
    void initWithValues(List<TableColumn> values);
    TableColumn getIDField();
}
