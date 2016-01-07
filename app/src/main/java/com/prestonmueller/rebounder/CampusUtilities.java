package com.prestonmueller.rebounder;

import java.util.ArrayList;

/**
 * Created by prestonmueller on 2/7/15.
 */
public class CampusUtilities {

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 3958.75;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double sindLat = Math.sin(dLat / 2);
        double sindLng = Math.sin(dLng / 2);
        double a = Math.pow(sindLat, 2) + Math.pow(sindLng, 2)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2));
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        return dist;
    }

    public static String locationWPI(double lat, double lng) {

        ArrayList<String> a = new ArrayList<String>();

        a.add("42.27414927609027,-71.81169390711148,Alumni Field");
        a.add("42.27579255268399,-71.81024551391602,Rooftop Field");
        a.add("42.27414133745202,-71.81067466768582,Sports and Rec Center");
        a.add("42.27445888220265,-71.80951595339138,Harrington Auditorium");
        a.add("42.273410978211515,-71.81069612519423,Morgan Hall");
        a.add("42.27324823402077,-71.80994510667006,Daniels Hall");
        a.add("42.273192662737436,-71.80903315548221,Riley Hall");
        a.add("42.27313709133589,-71.80837333210548,Alden Memorial");
        a.add("42.2737166185355,-71.8089097738266,Bartlett Center");
        a.add("42.27418896895513,-71.80827140807082,Higgins Labs");
        a.add("42.27389126952332,-71.80767595767975,Stratton Hall");
        a.add("42.27423263142021,-71.8075686692373,Project Center");
        a.add("42.27463750011032,-71.80710732928674,Salisbury Labs");
        a.add("42.27514953644179,-71.80791199207306,Olin Hall");
        a.add("42.27486374937009,-71.80797636508942,Olin Hall");
        a.add("42.27507015106644,-71.80827140808105,Campus Center");
        a.add("42.274895503345505,-71.80834650993347,Campus Center");
        a.add("42.2747208551406,-71.80859863758087,Campus Center");
        a.add("42.275534552740446,-71.80782616138458,Goddard Hall");
        a.add("42.275911628617756,-71.80791735649109,Goddard Hall");
        a.add("42.27523289041416,-71.8070590495654,Atwater Kent");
        a.add("42.274982827058814,-71.8064528702871,Fuller Labs");
        a.add("42.2749074109524,-71.80586278430383,Fuller Labs");
        a.add("42.27417706110868,-71.80632948867242,Library");
        a.add("42.273974625575185,-71.80509567244371,East Hall");
        a.add("42.2738356988762,-71.80459678156694,East Parking Garage");
        a.add("42.27308151978152,-71.80524051173052,Founders Hall");
        a.add("42.27348242674786,-71.80734872817993,Boynton Hall");
        a.add("42.27382775995615,-71.80973052978516,Quad");
        a.add("42.27368486358541,-71.8084108829089,Beech Tree Circle");
        a.add("42.272712365911495,-71.80640995502472,Skull Tomb");
        a.add("42.27269713865719,-71.80472284555435,Sigma Pi");
        a.add("42.275066181718906,-71.80112600293796,Faraday");
        a.add("42.275260675335524,-71.79893732005439,Gateway Park");
        a.add("42.27974576672568,-71.80743455821357,Salisbury Estates");
        a.add("42.2727996927362,-71.81179046598118,Stoddard Complex");
        a.add("42.27100549670643,-71.81486964221676,P'Chops");
        a.add("42.27707062659373,-71.80717706680298,Institute Park");
        a.add("42.276451438509184,-71.80595397949219,Institute Park");
        a.add("42.275975135842714,-71.80479526519775,Institute Park");
        a.add("42.275768736902634,-71.80359363555908,Institute Park");
        a.add("42.27120397214681,-71.80735945685228,Sole Proprietor");
        a.add("42.270858624320134,-71.8082982301712,Wooberry");
        a.add("42.27549089122925,-71.80884003639221,Higgins House");
        a.add("42.27234717900895,-71.8099182844162,International House");
        a.add("42.272180462568,-71.80793881416321,West Street House");
        a.add("42.273950809227976,-71.80584669047676,Boynton Lot");
        a.add("42.27248213950473,-71.80859327316284,West Lot");
        a.add("42.27285526424487,-71.8136465549469,Mobil Station");
        a.add("42.27541944486382,-71.80278897219978,Sig Ep");
        a.add("42.275038396763705,-71.80001020431519,Marriott Hotel");
        a.add("42.272752059750516,-71.81260585784912,Hackfeld House");
        a.add("42.261096802852585,-71.79490327835083,Union Station/Downtown");
        a.add("42.27676897162023,-71.8092584603437,First Baptist Church");
        a.add("42.34636520770291,-71.0870361328125,Not in Worcester! In Boston");
        a.add("42.285310011910866,-71.73076627543196,Shrewsbury");
        a.add("42.2752765520868,-72.08404541015625,Not in Worcester! In North Brookfield");

       String result = "";
        double closestValue = 999999;

        for(String b : a) {
            double bLat = Double.parseDouble(b.split(",")[0]);
            double bLng = Double.parseDouble(b.split(",")[1]);
            double distance = distFrom(bLat,bLng,lat,lng);
            if(distance < closestValue) {
                result = b.split(",")[2];
                closestValue = distance;
            }
        }

        return result;

    }
}
