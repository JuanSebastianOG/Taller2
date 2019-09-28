package com.example.tallerlocation;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

class MyLocation {
    private Date fecha;

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    private double latitud;
    private double longitud;

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public MyLocation() {
    }

    public JSONObject toJSON () {
        JSONObject obj = new JSONObject();
        try {
            obj.put("latitud", getLatitud());
            obj.put("longitud", getLongitud());
            obj.put("date", getFecha());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

}
