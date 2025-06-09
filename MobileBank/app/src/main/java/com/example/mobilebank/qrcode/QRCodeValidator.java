package com.example.mobilebank.qrcode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class QRCodeValidator {
    //mislim da ovaj validator treba pre nego sto se generise qr kod u banci 1 i quess
    public static String isQRCodeValid(String qrData) {
        if (qrData == null) {
            //mozda da se vraca neki string koji ce da kaze sta ne valja u qr kodu
            return "QR kod je prazan";
        }
        String[] lines = qrData.split("\\|");
        Map<String, String> qrMapa = new HashMap<>();
        for (String line : lines) {
            String[] delovi = line.split(":");
            qrMapa.put(delovi[0], delovi[1]);
        }

        if (qrMapa.containsKey("K")) {
            if (qrMapa.get("K") == null) {
                return "Moguce je generisati samo NBS IPS QR kod \"K\"";
            }
        } else {
            return "Moguce je generisati samo NBS IPS QR kod \"K\"";
        }

        if (qrMapa.containsKey("V")) {
            if (qrMapa.get("V") == null) {
                return "Nije u skladu sa formatom IPS QR koda. Tag koji se navede mora imati vrednost";
            } else if (!qrMapa.get("V").equals("01")) {
                return "Neispravan QR format, tag V mora biti 01, uneto" + qrMapa.get("V");
            }
        } else {
            return "Obavezan tag \"V\" nije naveden";
        }

        if (qrMapa.containsKey("C")) {
            if (qrMapa.get("C") == null) {
                return "Nije u skladu sa formatom IPS QR koda. Tag koji se navede mora imati vrednost";
            } else if (!qrMapa.get("C").equals("1")) {
                return "Neispravan format elementa, tag C mora imati oznaku 1 sto predstavlja UTF-8 znakovni skup, uneto:\'" + qrMapa.get("C") + "\'";
            }
        } else {
            return "Obavezan tag \"C\" nije naveden";
        }

        if (qrMapa.containsKey("R")) {
            if (qrMapa.get("R") == null) {
                return "Nije u skladu sa formatom IPS QR koda. Tag koji se navede mora imati vrednost";
            }
            //treba dodati else deo da se proveri broj racuna da li sadrzi dovoljno cifara i da li je tag po modelu 97
        } else {
            return "Obavezan tag \"R\" nije naveden";
        }

        if (qrMapa.containsKey("N")) {
            if (qrMapa.get("N") == null) {
                return "Nije u skladu sa formatom IPS QR koda. Tag koji se navede mora imati vrednost";
            } else if (qrMapa.get("N").length() > 70) {
                return "Neispravan format elementa. Broj karaktera u tagu N ne sme biti veci od 70. Uneto:\'" + qrMapa.get("N") + "\'";
            }
        } else {
            return "Obavezan tag \"N\" nije naveden";
        }

        if (qrMapa.containsKey("I")) {
            if (qrMapa.get("I") == null) {
                return "Nije u skladu sa formatom IPS QR koda. Tag koji se navede mora imati vrednost";
            } else if (!qrMapa.get("I").startsWith("RSD")) {
                return "Neispravan format elementa. Neispravan format taga \"I\". Uneto:\'" + qrMapa.get("I") + "\'";
            } else {
                String tmp = qrMapa.get("I").replace("RSD", "");
                if (tmp.split(",")[1] == null || tmp.split(",")[1].length() > 2) {
                    return "Neispravan format elementa. Neispravan format taga \"I\". Uneto:\'" + qrMapa.get("I") + "\'";
                }
            }
        } else {
            return "Obavezan tag \"I\" nije naveden";
        }

        if (qrMapa.containsKey("SF")) {
            if (qrMapa.get("SF") == null) {
                return "Nije u skladu sa formatom IPS QR koda. Tag koji se navede mora imati vrednost";
            } else if (!(qrMapa.get("SF").startsWith("1") || qrMapa.get("SF").startsWith("2")) || qrMapa.get("SF").length() != 3) {
                return "Neispravan format elementa. Sifra placanja nije ispravna. Dozvoljene 3 cifre po propisu. Za racune/fakture prva mora biti 1 ili 2. Uneto:\'"+ qrMapa.get("SF")+ "\'";
            }
        } else {
            return "Obavezan tag \"SF\" nije naveden";
        }

        return "Ispravan QR kod";
    }
}
