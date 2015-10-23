package Classes;

public class Calculo
{
    /*public static void main(String args[])
    {
        double latA = Calculo.converte(-28.92959586);
        double logA = Calculo.converte(-52.12371077);
        
        double latB = Calculo.converte(-28.92976559);
        double logB = Calculo.converte(-52.1235946);
        
        System.out.println(Calculo.getDistancia(latA, logA, latB, logB));
    }*/

    public static double getDistancia(double latitude, double longitude, double latitudePto, double longitudePto)
    {  
        double dlon, dlat, a, distancia;  
        dlon = longitudePto - longitude;  
        dlat = latitudePto - latitude;  
        a = Math.pow(Math.sin(dlat/2),2) + Math.cos(latitude) * Math.cos(latitudePto) * Math.pow(Math.sin(dlon/2),2);  
        distancia = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));  
        
        return 6378140 * distancia; /* 6378140 is the radius of the Earth in meters*/  
    }
    
    public static double converte(double aux) 
    {  
        int graus = (int) aux/100;  
        int minutos = ((int)aux%100)/60;  
        double segundos = (aux - (int) aux)/36.0;  
          
        aux = graus + minutos + segundos;  
          
        return aux;  
    }
}