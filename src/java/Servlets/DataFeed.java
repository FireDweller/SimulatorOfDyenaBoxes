package Servlets;

import Classes.DataFileHandler;
import Classes.Message;
import Classes.DyenaBoxesHandler;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Handles http requests and responses. Outputs messages received from <code>DyenaBoxesHandler</code>.
 * 
 * @author Ian Weeks and Vilius Plepys
 */
public class DataFeed extends HttpServlet
{
    DataFileHandler fileHandler;
    Gson gson;
    DyenaBoxesHandler boxes;
    int DyenaBoxControllerIndex;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @SuppressWarnings("empty-statement")
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        response.setContentType("text/html;charset=UTF-8");
          try (PrintWriter out = response.getWriter()) {
             
              String output = gson.toJson(boxes.getMessagesToDisplay());
              out.println(output);          

          }    
    }

    /**
     * Sets attributes to Servlet Context, activates a <code>DienBoxHandler</code> object in JSON format. 
     * @exception ServletException when specific to servlets error occurs
     */
    @Override
    public void init() throws ServletException
    {

        fileHandler = new DataFileHandler();
        fileHandler.lookupForFiles("C:\\Users\\vplep\\Desktop\\MComp_project\\resources\\");
        //fileHandler.lookupForFiles("/usr/local/wildfly-8.2.0.Final/standalone/deployments/dyena/resources/");
        gson = new Gson();
        boxes = new DyenaBoxesHandler();
        
        ServletContext sc = getServletContext();
        sc.setAttribute("fH", fileHandler);
        sc.setAttribute("DBoxes", boxes);
        sc.setAttribute("DBoxContrI",DyenaBoxControllerIndex);
        
        Thread thread = new Thread(boxes);
        
        thread.start();
        super.init();

    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo()
    {
        return "Short description";
    }// </editor-fold>
}
