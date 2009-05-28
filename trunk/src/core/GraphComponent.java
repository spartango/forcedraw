/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package core;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;
import javax.swing.JComponent;

/**
 *
 * @author anand
 */
public class GraphComponent extends JComponent{
    public static final int POINT_SIZE = 2;
    private double xValue;
    private double yValue;
    private Vector xHistory;
    private Vector yHistory;

    private Vector markers;
    private double max;
    public GraphComponent() {
        xHistory = new Vector();
        yHistory = new Vector();
        markers = new Vector();
        max = 1;
        //this.setBackground(Color.BLACK);
        
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void placeMarker(double xVal){
        markers.addElement(new Double(xVal));
    }

    public void removeMarker(int no){
        markers.removeElementAt(no);
    }

    public void clearMarkers(){
        markers.removeAllElements();
    }

    public Vector getMarkers() {
        return markers;
    }
    
    //½@Override
    public void paintComponent(Graphics g){
        
        try{
        g.setColor(Color.BLACK);
        g.fill3DRect(0, 0, this.getWidth(), this.getHeight(), false);
        g.setColor(Color.RED);
        for(int m =0; m<markers.size(); m++){
            g.fillRect(((Double) markers.elementAt(m)).intValue()%this.getWidth(), 0, POINT_SIZE, this.getHeight());
        }
        g.setColor(Color.WHITE);
        for(int i=xHistory.size()-1; xHistory.size()-i<=this.getWidth() && i>=0; i--){
            g.drawRect(((Double)(xHistory.elementAt(i))).intValue()%this.getWidth(),
                    (int) (this.getHeight()*(1-(((Double)(yHistory.elementAt(i))).intValue()/max)) -POINT_SIZE),
                    POINT_SIZE, POINT_SIZE);
            if(i>0)
            g.drawLine(((Double)(xHistory.elementAt(i))).intValue()%this.getWidth(),
                    (int) (this.getHeight()*(1-(((Double)(yHistory.elementAt(i))).intValue()/max))),
                    ((Double)(xHistory.elementAt(i-1))).intValue()%this.getWidth(),
                    (int) (this.getHeight()*(1-(((Double)(yHistory.elementAt(i-1))).intValue()/max))));
        }
        g.setColor(Color.GREEN);
        g.fillRect((int) xValue%this.getWidth(), (int) (this.getHeight()*(1-(yValue/max))) -POINT_SIZE, POINT_SIZE+2, POINT_SIZE+2);
        }catch(Exception e){
            System.out.println("----");
        }
        g.setColor(Color.WHITE);
        g.drawString("("+xValue +" , " +yValue*3+")",5,11);
    }

    public void addPoint(double x, double y){
        setXValue(x);
        setYValue(y);
    }

    public void reset(){
        xHistory.removeAllElements();
        yHistory.removeAllElements();
    }

    public Vector getXHistory() {
        return xHistory;
    }

    public Vector getYHistory() {
        return yHistory;
    }

    public double getXValue() {
        return xValue;
    }

    public void setXValue(double xValue) {
        this.xValue = xValue;
        if(xHistory.size() >= this.getWidth())
            xHistory.removeAllElements();
        xHistory.addElement(new Double(xValue));
    }

    public double getYValue() {
        return yValue;
    }

    public void setYValue(double yValue) {
        this.yValue = yValue;
        if(yHistory.size() >= this.getWidth())
            yHistory.removeAllElements();
        yHistory.addElement( new Double(yValue));
    }
    
}
