
package Servlets;

import Classes.DataFileHandler;
import Classes.DyenaBoxesHandler;
import java.io.IOException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Controller that receives data from and sends data to the user interface.
 * 
 * @author Vilius
 */
@WebServlet(name = "DyenaBox1Controller", urlPatterns = {"/DyenaBoxController"})
public class DyenaBoxesController extends HttpServlet {

    ServletContext sc;
    DataFileHandler fH;
    DyenaBoxesHandler boxes;
    Integer fileIndex;
    Integer boxNo;
    Integer numberOfBoxesToShow;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        sc = getServletContext();
        boxes = (DyenaBoxesHandler) sc.getAttribute("DBoxes");
        fH = (DataFileHandler) sc.getAttribute("fH"); 
        boxNo = Integer.parseInt(request.getParameter("BoxNo"))-1;
  

        
/*--Request for file assignment-----------------------------------------------*/        
        if (request.getParameter("assign") != null) {        
            fileIndex = Integer.parseInt(request.getParameter("chosenFile"));
            boxes.getBox(boxNo).assignDataFile(fH.getFile(fileIndex));
            boxes.getBox(boxNo).setAssignedDataFileIndex(fileIndex);
            fH.setAvailability(fileIndex, Boolean.FALSE);         
            boxes.getBox(boxNo).setFileAssigned(true);
        }
        
/*--Request for file unasignment:---------------------------------------------*/  
        if (request.getParameter("unassign") != null) {
           
            fH.setAvailability( boxes.getBox(boxNo).getAssignedDataFileIndex(), Boolean.TRUE);
            boxes.getBox(boxNo).setFileAssigned(false);
        }

/*--Request for starting the simulation:--------------------------------------*/ 
        if (request.getParameter("start") != null) {     
            boxes.getBox(boxNo).setSimulationStatus(Boolean.TRUE);    
        }

/*--Request for stopping the simulation:--------------------------------------*/          
         if (request.getParameter("stop simulation") != null) {        
            boxes.getBox(boxNo).setSimulationStatus(Boolean.FALSE);    
            boxes.getBox(boxNo).clearMessage();
         }

/*--Request for blackout:-----------------------------------------------------*/   
         if (request.getParameter("blackout") != null) {        
            boxes.getBox(boxNo).setBlackout(Boolean.TRUE);
            boxes.getBox(boxNo).clearMessage();
         }
  
/*--Request to stop blackout:-------------------------------------------------*/  
         if (request.getParameter("stop blackout") != null) {        
            boxes.getBox(boxNo).setBlackout(Boolean.FALSE);
         }
          
         request.getRequestDispatcher("interface.jsp").forward(request, response);
       

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
            throws ServletException, IOException {
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
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
