import java.util.*;
import java.io.*;

class Piece {
    String letter;
    int row, col;
    List<List<Integer>> shape;
    
    // constructor
    public Piece(String letter, int row, int col, List<List<Integer>> shape) {
    this.letter = letter;
    this.row = row;
    this.col = col;
    this.shape = shapePadding(shape);  // shape di padding dengan 0 pada bagian kosong agar berbentuk persegi /+ panjang
    }
    
    // method untuk padding shape
    private List<List<Integer>> shapePadding(List<List<Integer>> shape) {
        for (List<Integer> shapeRow : shape) {
            int delta = this.col - shapeRow.size();
            for (int i = 0; i < (delta); i++) {
                shapeRow.add(0);
            }
        }
        return shape;
    }
    
    
    // method untuk menampilkan piece (debugging)
    public void getPiece(){
        System.out.println("======================");
        System.out.println("Letter : " + this.letter);
        System.out.println("row : " + this.row);
        System.out.println("col : " + this.col);
        System.out.println("shape : ");
        for (int i = 0; i < this.shape.size(); i++){
            for (int j = 0; j < this.shape.get(i).size(); j++) {
                System.out.print(this.shape.get(i).get(j) + " ");                
            }
            System.out.println();
        }
    }
    
    // method untuk merotasi piece
    private void rotate(){
        List<List<Integer>> rotated = new ArrayList<>();
        for (int j = this.col - 1; j > -1; j--){
            List<Integer> rotatedRow = new ArrayList<>();
            for (int i = 0; i < this.row; i++){
                rotatedRow.add(this.shape.get(i).get(j));
            }
            rotated.add(rotatedRow);
        }
        this.shape = rotated;
        this.row = rotated.size();
        this.col = rotated.get(0).size();
    }
    
    // method untuk mirroing piece
    private void mirror(){
        List<List<Integer>> mirrored = new ArrayList<>();
        for (int i = 0; i < this.row; i++){
            List<Integer> mirroredRow = new ArrayList<>();
            for (int j = this.col - 1; j > -1; j--){
                mirroredRow.add(this.shape.get(i).get(j));
            }
            mirrored.add(mirroredRow);
        }
        this.shape = mirrored;
        this.row = mirrored.size();
        this.col = mirrored.get(0).size();
    }
    
    // method untuk menghasilkan 8 kemungkinan posisi piece setelah di rotasi dan mirroring
    public List<List<List<Integer>>> getPositions(){
        List<List<List<Integer>>> pos = new ArrayList<>();
        pos.add(this.shape);
        // rotasi
        for (int i = 0; i < 3; i++){
            this.rotate();
            pos.add(this.shape);
        }
        // mirror
        this.mirror();
        pos.add(this.shape);
        // rotasi mirror
        for (int i = 0; i < 3; i++){
            this.rotate();
            pos.add(this.shape);
        }
        return pos;
    }
}

class Board {
    int row, col;
    List<List<Integer>> flag; // board dalam bentuk 0 1
    List<List<String>> shape; // board dalam bentuk huruf
    
    // constructor
    public Board(int row, int col){
        this.row = row;
        this.col = col;
        
        List<List<Integer>> flag = new ArrayList<>();
        List<List<String>> shape = new ArrayList<>();
        for (int i = 0; i < row; i++){
            List<Integer> flagRow = new ArrayList<>();
            List<String> shapeRow = new ArrayList<>();
            for (int j = 0; j < col; j++){
                flagRow.add(0);
                shapeRow.add(" ");
            }
            flag.add(flagRow);
            shape.add(shapeRow);
        }
        this.flag = flagPadding(flag);  // di padding biar aman kalo indexOutOfBound
        this.shape = shape;
    }
    
    // method untuk padding
    private List<List<Integer>> flagPadding(List<List<Integer>> flag){
        int max = Math.max(this.row, this.col);
        for (int i = 0; i < this.row; i++){
            for (int j = 0; j < max; j++){
                flag.get(i).add(1);
            }
        }
       
        for (int i = 0; i < max; i++){
            List<Integer> padRow = new ArrayList<>();
            for (int j = 0; j < this.row + max; j++){
                padRow.add(1);
            }
            flag.add(padRow);
        }
        return flag;
    }
    
    // method untuk nampilin shape
    public void showBoardShape(){
        for (List<String> row : this.shape){
            for (String c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
    
    // method untuk nampilin flag
    public void showBoardFlag(){
        for (List<Integer> row : this.flag){
            for (Integer c : row) {
                System.out.print(c + " ");
            }
            System.out.println();
        }
    }
    
    // method untuk masukin piece ke board
    public void install(List<List<Integer>> shape, int rowOffset, int colOffset, String letter){
        for (int i = 0; i < shape.size(); i++){
            for (int j = 0; j < shape.get(0).size(); j++){
                if (shape.get(i).get(j) == 1){
                    this.flag.get(i + rowOffset).set(j + colOffset, 1);
                    this.shape.get(i + rowOffset).set(j + colOffset, letter);
                }
            }
        }
    }
    
    // method untuk ngeluarin piece dari board
    public void uninstall(List<List<Integer>> shape, int rowOffset, int colOffset){
        for (int i = 0; i < shape.size(); i++){
            for (int j = 0; j < shape.get(0).size(); j++){
                if (shape.get(i).get(j) == 1){
                    this.flag.get(i + rowOffset).set(j + colOffset, 0);
                    this.shape.get(i + rowOffset).set(j + colOffset, " ");
                }
            }
        }
    }
}


public class Main {
    // fungsi untuk baca input
    public static Object[] readFromFile(String filePath) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(filePath));

            // Baris pertama
            String[] firstLine = br.readLine().split(" ");
            int m = Integer.parseInt(firstLine[0]);
            int n = Integer.parseInt(firstLine[1]);
            int p = Integer.parseInt(firstLine[2]);

            // Baris kedua
            String mode = br.readLine();

            // custom mode
            List<List<Integer>> customBoardFlag = new ArrayList<>();
            if (mode.equals("CUSTOM")){
                for (int i = 0; i < m; i++){
                    String line = br.readLine();
                    List<Integer> customRow = new ArrayList<>();
                    for (char cell : line.toCharArray()){
                        if (cell == 'X'){
                            customRow.add(1);
                        } else {  // cell == '.'
                            customRow.add(0);
                        }
                    }
                    customBoardFlag.add(customRow);
                }
            }

            // List warna
            String[] colors = {
                "\u001b[31m", // Merah
                "\u001b[32m", // Hijau
                "\u001b[33m", // Kuning
                "\u001b[34m", // Biru
                "\u001b[35m", // Magenta
                "\u001b[36m", // Cyan
                "\u001b[37m", // Putih
                "\u001b[91m", // Merah Terang
                "\u001b[92m", // Hijau Terang
                "\u001b[93m", // Kuning Terang
                "\u001b[94m", // Biru Terang
                "\u001b[95m", // Magenta Terang
                "\u001b[96m", // Cyan Terang
                "\u001b[97m", // Putih Terang
                "\u001b[90m"  // Hitam Terang
            };

            // Baris ketiga
            List<Piece> pieces = new ArrayList<>();
            List<List<Integer>> currShape = new ArrayList<>();
            char currLetter = ' ';
            int row = 0;
            int col = 0;
            int colorIndex = 0;
            String line;
            while((line = br.readLine()) != null) {
                line = line.replaceAll("\\s+$", "");
                if (line.isEmpty()) continue;
                
                char firstChar = line.replaceAll("^\\s+", "").charAt(0);
                
                if (currLetter == ' ') {
                    currLetter = firstChar;
                }
                
                if (firstChar != currLetter) {
                    // buat piece baru
                    pieces.add(new Piece(colors[colorIndex] + currLetter + "\u001b[0m", row, col, currShape));
                    currLetter = firstChar;
                    currShape = new ArrayList<>();
                    row = 0;
                    col = 0;
                    colorIndex++;
                    colorIndex %= 15;
                }
                
                List<Integer> currShapeRow = new ArrayList<>();
                int currCol = 0;
                for (char c : line.toCharArray()) {
                    if (c == ' ') {
                        currShapeRow.add(0);                       
                    } else {
                        currShapeRow.add(1);
                    }
                    currCol++;
                }
                
                currShape.add(currShapeRow);
                col = Math.max(col, currCol);
                row++;
            }
            
            // menambahkan piece terakhir
            if (!currShape.isEmpty()) {
                pieces.add(new Piece(colors[colorIndex] + currLetter + "\u001b[0m", row, col, currShape));
            }
            
            br.close();
            


            return new Object[]{pieces, m, n, p, mode, customBoardFlag};
        } catch (IOException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }

    // fungsi untuk menulis output ke file
    public static void writeToFile(Board board, String filePath){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))){
            for (List<String> row : board.shape){
                for (String s : row){
                    writer.write(s.replaceAll("\\u001b\\[[0-9;]*m", "") + " ");
                }
                writer.newLine();
            }
            System.out.println("Output berhasil ditulis ke file: " + filePath);
        } catch (IOException e) {
            
            System.out.println("Output Gagal ditulis ke file: " + filePath);
        }
    }
    
    // fungsi untuk mengecek apakah piece bisa masuk ke board
    public static boolean isMatch(Board board, List<List<Integer>> shape, int rowOffset, int colOffset){
        try {
            if ((rowOffset < 0) || (colOffset < 0)) {return false;}
            
            int cell;
            for (int i = 0; i < shape.size(); i++) {
                for (int j = 0; j < shape.get(0).size(); j++){
                    cell = board.flag.get(rowOffset + i).get(colOffset + j) + shape.get(i).get(j);
                    if (cell > 1) {return false;}
                }
            }
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            
            return false;
        }
    }
    
    // fungsi utama, melakukan bruteforcing dan backtracking untuk menemukan solusi
    public static Object[] bruteForce(Board board, List<Piece> pieces, int count) {
        for (int rowOffset = 0; rowOffset < board.row; rowOffset++){
            for (int colOffset = 0; colOffset < board.col; colOffset++){
                if (board.flag.get(rowOffset).get(colOffset) == 0) { // jika ditemukan space kosong pada board
                    for (Piece currPiece : pieces){
                        String letter = currPiece.letter;
                        for (List<List<Integer>> shape : currPiece.getPositions()){
                            for (int rowPointer = 0; rowPointer < shape.size(); rowPointer++){
                                for (int colPointer = 0; colPointer < shape.get(0).size(); colPointer++){
                                    count++;
                                    if (isMatch(board, shape, rowOffset - rowPointer, colOffset - colPointer)){  // jika piece bisa masuk ke board
                                        board.install(shape, rowOffset - rowPointer, colOffset - colPointer, letter);


                                        
                                        List<Piece> nextPieces = new ArrayList<>();
                                        for (Piece pc : pieces){
                                            if (!pc.equals(currPiece)){
                                                nextPieces.add(pc);  // nextPieces berisi semua piece kecuali piece saat ini
                                            }
                                        }
                                        Object[] result = bruteForce(board, nextPieces, count);  // rekurens
                                        boolean isComplete = (boolean)result[0];
                                        count = (int)result[1];
                                        if (isComplete){
                                            return new Object[]{true, count};  // basis
                                        }
                                        board.uninstall(shape, rowOffset - rowPointer, colOffset - colPointer);
                                    }
                                }
                            }
                        }
                    }
                    return new Object[]{false, count};
                }
            }
        }
        return new Object[]{true, count}; // basis
    }
    
    @SuppressWarnings("unchecked")  // tipe list dipastikan sesuai, ga perlu di cek
    public static void main(String[] args) {

        try {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Masukkan nama file input : ");
            String filename = scanner.nextLine().strip();
            

            Object[] result = readFromFile("test/" + filename);
            
            List<Piece> pieces = (List<Piece>)  result[0];
            int m = (int) result[1];
            int n = (int) result[2];
            int p = (int) result[3];

            
            String mode = (String) result[4];
            
            List<List<Integer>> customBoardFlag = (List<List<Integer>>) result[5];
            
            // membuat object board
            Board board = new Board(m, n);
            
            if (mode.equals("CUSTOM")){
                for (int i = 0; i < m; i++){
                    for (int j = 0; j < n; j++){
                        if (customBoardFlag.get(i).get(j) == 0){
                            board.flag.get(i).set(j, 1);
                        }
                    }
                }
            }
    
            
            int count = 0;
            long startTime = System.currentTimeMillis();
            Object[] values = bruteForce(board, pieces, count);
            long endTime = System.currentTimeMillis();
            
            boolean isComplete = (boolean) values[0];
            count = (int) values[1];
            
            if (isComplete){
                System.out.println("\nBerhasil mengisi board\n");
                board.showBoardShape();
            } else {
                System.out.println("\nGagal mengisi board\n");
            }
            
            System.out.println("\nJumlah percobaan : " + count);
            System.out.println("Waktu eksekusi : " + (endTime - startTime) + " ms");
            
            if (isComplete){

                while (true){
                    System.out.println("Apakah anda ingin menyimpan solusi?(y/n)");
                    String ans = scanner.nextLine().strip();
            
                    if (ans.equals("y") | ans.equals("Y")) {
                        System.out.print("nama file: ");
                        String filePath = scanner.nextLine().strip();
                        writeToFile(board, "output/" + filePath);
                        break;
                    } else if (ans.equals("n") | ans.equals("N")){
                        break;
                    }

                }
        
        
            scanner.close();
    
            }
        } catch ( IndexOutOfBoundsException | NullPointerException | NumberFormatException e) {
            System.out.println("\nPastikan nama dan isi file input sudah benar!");
        }


    }
}


