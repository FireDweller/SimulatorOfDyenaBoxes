<%-- 
    Document   : DyenaBoxes
    Created on : 24-Mar-2016, 19:54:19
    Author     : Vilius
--%>

<%@page import="Classes.DyenaBoxesHandler"%>
<%@page import="Classes.DataFileHandler"%>
<%@page import="javax.xml.transform.Source"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">

<!--[if lt IE 9]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js">
</script>
<![endif]-->

<style>

 table, td, th {
    border: 1px solid black;
    border-style: solid;
    border-color: #269CDA;
}

table {
    
    background-color: #C5D4DB; 
}

body {
     background-image: url(background.png);
     background-size: cover;
}


th, td {
    text-align: center;
}

tr {
    height: 60px;
}

h2
{
    color:darkblue;
    
}

#box_number_column {
    width: 60px;
}


</style>

</head>


<body> 
 
    <header style="text-align:center" > <h2> Simulator of Dyena boxes : interface </h2></header>
  
    <%
        DataFileHandler fH = (DataFileHandler) application.getAttribute("fH");
        DyenaBoxesHandler DBoxes = (DyenaBoxesHandler) application.getAttribute("DBoxes");      
    %>   

          
      <div class="container">  
            </div>

        <table style = "width: 725px " align = "center">
            <tr style = "height: 37px ">    
                <th scope="col" id="box_number_column"> box # </th>
                <th scope="col" style="width:266px"> status </th>            	           
                <th scope="col"  colspan="2" style="width:411px" > action </th>
            </tr>
            

<% for (int boxI = 1; boxI < 11; boxI++){%>

<tr style="height:60px">
                <td><%=boxI%></td>
           
 <!--Dyena box status section:beginning----------------------------------------------------------------------------------------------------------------------------------------->
                <td >
                    <%  
                        if (DBoxes.getBox(boxI-1).isFileAssigned()) {
                            out.println("<font color=\"white\"> file " + DBoxes.getBox(boxI - 1).getDataFile().getName() + " is assigned </font></br>");   
                            }                       
                            
                        if (DBoxes.getBox(boxI-1).isSimulated()) {
                                out.println("<font color=\"yellow\"> is simulated </br></font>");
                            } 
                      
                        if (DBoxes.getBox(boxI-1).isBlackout()) {
                            out.println("<font color=\"darkmagenta\">blackout</font>");
                            }  
                        
                        if (!DBoxes.getBox(boxI-1).isFileAssigned() && !DBoxes.getBox(boxI-1).isSimulated() && !DBoxes.getBox(boxI-1).isBlackout())
                            {
                             out.println("idle");        
                            }                        
                    %>
                </td>
<!--Dyena box status section:ending----------------------------------------------------------------------------------------------------------------------------------------->
 
<!--If file is not assigned condition:beginning-------------------------------------------------------------------------------------------------------------------------------------------------->
    <% if (!DBoxes.getBox(boxI-1).isFileAssigned()) {%>
                <td colspan="2">                               
                    <form action="DyenaBoxesController" method="post">
                            <select name="chosenFile" >                  
                            <%
                                for (int i = 0; i < fH.getFiles().length; i++) {/*2*/
                                    if (fH.isAvailable(i)) {
                                        out.println("<option value=\"" + i + "\">" + fH.getFile(i).getName() + "</option>");
                                    }
                                    /*2*/}
                             %>                           
                            </select>
                            <input type="hidden" name="BoxNo" value="<%=boxI%>" >
                            <input type="submit" name="assign" value="assign file" >
                       </form>                    
                 </td>   
    <%}       
//<!--If file is not assigned condition:ending-------------------------------------------------------------------------------------------------------------------------------------------------->
    
    else if (!DBoxes.getBox(boxI-1).isSimulated()){ %>
      <td>  
            <form action="DyenaBoxesController" method="post">                                                                                       
                  <input type="hidden" name="BoxNo" value="<%=boxI%>" >
                  <input type="submit" name="unassign" value="unassign file" >          
            </form>       
      </td>   
      
      <td>
          <form action="DyenaBoxesController" method="post">
                   <input type="hidden" name="BoxNo" value="<%=boxI%>" >
                   <input type="submit" name="start" value="start simulation">
        </form>
      </td>
      
     <%} else if (DBoxes.getBox(boxI-1).isSimulated()&& !DBoxes.getBox(boxI-1).isBlackout()){ %>
        
       <td>   
            <form action="DyenaBoxesController" method="post">
                   <input type="hidden" name="BoxNo" value="<%=boxI%>" >
                   <input type="submit" name="stop simulation" value="stop simulation">
            </form>
        </td>
       
        <td> 
             <form action="DyenaBoxesController" method="post">
                 <input type="hidden" name="BoxNo" value="<%=boxI%>" >
                 <input type="submit" name="blackout" value="start blackout">
            </form>
        </td>
      
        <%} else if (DBoxes.getBox(boxI-1).isBlackout()){%>
       
        <td  colspan="2" >  
            <form action="DyenaBoxesController" method="post">
                <input type="hidden" name="BoxNo" value="<%=boxI%>" >
                <input type="submit" name="stop blackout" value="stop blackout">
            </form>
        </td>
           
        <%}%>
           
<%}%><!--End of the outer loop.-->

</tr>
 </table>  


 </body>
 </html>
