import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * A six sided toggle button. This is not guaranteed to be a perfect hexagon, it is just guaranteed to have six sides in
 * the form of a hexagon. To be a perfect hexagon the size of this component must have a height to width ratio of
 * 1 to 0.866
 *
 * @author keang
 * @date 5 Jun 2009
 *
 */
public class HexButton extends JButton
{
    private static final long serialVersionUID = 4865976127980106774L;

    private Polygon hexagon = new Polygon();

    private Field field;

    public HexButton(Field field, Point coords, int size)
    {
        this.field = field;
        field.setButton(this);
        setForeground(Color.black);
        setBackground(Color.green);
        setBounds((int) ( coords.x*size*6/5), 100+(coords.y*size*2), (int) (1.73205081*size), 2*size);
        Integer num = field.GetNumberOfSheep();
        setText(num.toString());
    }

    public Field getField() {
        return field;
    }

    public void editLegalSteps(boolean visible){
        for (Field step:
                field.LegalSteps()){
            if(step != null){
                step.getButton().setSelected(visible);
            }
        }
    }

    public void setNumber(Integer number){
        setText(number.toString());
    }

    @Override
    public boolean contains(Point p)
    {
        return hexagon.contains(p);
    }

    @Override
    public boolean contains(int x, int y)
    {
        return hexagon.contains(x, y);
    }

    @Override
    public void setSize(Dimension d)
    {
        super.setSize(d);
        calculateCoords();
    }

    @Override
    public void setSize(int w, int h)
    {
        super.setSize(w, h);
        calculateCoords();
    }

    @Override
    public void setBounds(int x, int y, int width, int height)
    {
        super.setBounds(x, y, width, height);
        calculateCoords();
    }

    @Override
    public void setBounds(Rectangle r)
    {
        super.setBounds(r);
        calculateCoords();
    }




    public void stepFromSelection(boolean selected){
        super.setSelected(selected);
        field.setSelected(selected);
        editLegalSteps(selected);
    }

    @Override
    protected void processMouseEvent(MouseEvent e)
    {
        if ( contains(e.getPoint()) && e.getID() == MouseEvent.MOUSE_CLICKED) {
            if (getField().isStepFrom()){
                stepFromSelection(false);
            }
            else {
                if(getField().getBoard().getStepFrom() != null) {
                    getField().getBoard().getStepFrom().getButton().stepFromSelection(false);
                }
                stepFromSelection(true);
            }
        }
    }



    private void calculateCoords()
    {
        int w = getWidth()-1;
        int h = getHeight()-1;

        int ratio = (int)(h*.25);
        int nPoints = 6;
        int[] hexX = new int[nPoints];
        int[] hexY = new int[nPoints];

        hexX[0] = w/2;
        hexY[0] = 0;
        hexX[1] = w;
        hexY[1] = ratio;
        hexX[2] = w;
        hexY[2] = h - ratio;
        hexX[3] = w/2;
        hexY[3] = h;
        hexX[4] = 0;
        hexY[4] = h - ratio;
        hexX[5] = 0;
        hexY[5] = ratio;

        hexagon = new Polygon(hexX, hexY, nPoints);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        if ( isSelected() )
        {
            g.setColor(Color.lightGray);
        }
        else
        {
            g.setColor(getBackground());
        }

        g.fillPolygon(hexagon);

        g.setColor(getForeground());

        g.drawPolygon(hexagon);

        FontMetrics fm = getFontMetrics(getFont());
        Rectangle viewR = getBounds();
        Rectangle iconR = new Rectangle();
        Rectangle textR = new Rectangle();


        SwingUtilities.layoutCompoundLabel(this, fm, getText(), null,
                SwingUtilities.CENTER, SwingUtilities.CENTER, SwingUtilities.BOTTOM, SwingUtilities.CENTER,
                viewR, iconR, textR, 0);

        Point loc = getLocation();
        g.drawString(getText(), textR.x-loc.x, textR.y-loc.y+fm.getAscent());
    }

    @Override
    protected void paintBorder(Graphics g)
    {
        // do not paint a border
    }




}