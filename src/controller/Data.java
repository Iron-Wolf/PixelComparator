package controller;

import java.util.HashMap;

public class Data {
    public static final HashMap<String, String> hmap = createMap();

    private static HashMap<String,String> createMap(){
        HashMap<String,String> hmap = new HashMap<String, String>();
        hmap.put("FFFFFF","blanc");
        hmap.put("FEFADB","creme");
        hmap.put("FFFF00","jaune");
        hmap.put("FE8800","orange");
        hmap.put("FF0000","rouge");
        hmap.put("FDADD5","rose");
        hmap.put("A70CF4","violet");
        hmap.put("0066FF","bleu");
        hmap.put("0000FF","bleu_fonnce");
        hmap.put("0F9700","vert");
        hmap.put("5CF4B2","vert_clair");
        hmap.put("593000","marron");
        hmap.put("898989","gris");
        hmap.put("000000","noir");
        hmap.put("832300","caramel");
        hmap.put("C66C05","marron_clair");
        hmap.put("C66C05","chair");
        hmap.put("F9DCBC","beige");
        hmap.put("03492B","vert_fonce");
        hmap.put("890A0A","bordeaux");
        hmap.put("37BDD8","turquoise");
        hmap.put("F900E0","fuchsia_neon");
        hmap.put("FFFFCC","jaune_pastel");
        hmap.put("F79696","rouge pastel");
        hmap.put("CFA2F7","violet_pastel");
        hmap.put("00CCFF","bleu_pastel");
        hmap.put("84FD88","vert_pastel");
        hmap.put("FEC0EC","rose pastel");
        hmap.put("00E0E8","azure");
        hmap.put("F6B214","marron_nounours");
        hmap.put("BFBFBF","gris_clair");
        hmap.put("515151","gris_fonce");
        hmap.put("F21164","lie_de_vin");

        return hmap;
    }
}
