package alghoritms.model.environment;


public class Table {
    private TableQuadrant[][] table = new TableQuadrant[8][8];

    public Table(){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                table[i][j] = new TableQuadrant(i, j);
            }
        }
    }

    public Table(Table originalTable){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                table[i][j] = new TableQuadrant(originalTable.getTable()[i][j]);
            }
        }
    }


    public TableQuadrant[][] getTable() {
        return table;
    }

    public void setTable(TableQuadrant[][] table) {
        this.table = table;
    }



}
