package Ressources;

import java.util.HashMap;

public class Data {
    public static final HashMap<String, String> hmap = createMap();

    private static HashMap<String,String> createMap(){
        HashMap<String,String> hmap = new HashMap<String, String>();
        hmap.put("FFFFFF","blanc");
        hmap.put("FFF0C5","creme");
        hmap.put("FFFF00","jaune");
        hmap.put("E25618","orange");
        hmap.put("FF0000","rouge");
        hmap.put("EA899C","rose");
        hmap.put("A70CF4","violet");
        hmap.put("0072B3","bleu");
        hmap.put("045692","bleu fonce");
        hmap.put("00843A","vert");
        hmap.put("4AB27F","vert clair");
        hmap.put("41261F","marron");
        hmap.put("83888C","gris");
        hmap.put("000000","noir");
        hmap.put("93331A","caramel");
        hmap.put("C76E2C","marron clair");
        hmap.put("E7AE9A","chair");
        hmap.put("E6B97E","beige");
        hmap.put("2D3F31","vert fonce");
        hmap.put("503033","bordeaux");
        hmap.put("58AACF","turquoise");
        hmap.put("D8205E","fuchsia_neon");
        hmap.put("F2D934","jaune pastel");
        hmap.put("E8645F","rouge pastel");
        hmap.put("9F83B6","violet pastel");
        hmap.put("4AB8EB","bleu pastel");
        hmap.put("8FC474","vert pastel");
        hmap.put("FFE4FF","rose pastel");
        hmap.put("21A9BF","azure");
        hmap.put("E6A823","marron nounours");
        hmap.put("98B6BE","gris clair");
        hmap.put("4A4E4F","gris fonce");
        hmap.put("C71141","lie de vin");

        return hmap;
    }

    public static final String appName = "Pixel Comparator";
}
