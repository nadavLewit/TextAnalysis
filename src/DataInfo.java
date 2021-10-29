public class DataInfo {
    String filename;
    double fleschScore, kincaidScore;

    public DataInfo(String filename, double fleschScore, double kincaidScore){
        this.filename = filename;
        this.fleschScore = fleschScore;
        this.kincaidScore = kincaidScore;
    }
    public String getFileName(){return filename;}

    public double getFleschScore(){return fleschScore;}

    public double getKincaidScore(){return kincaidScore;}
}
