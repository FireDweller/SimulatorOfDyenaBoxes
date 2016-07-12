<%-- 
    Document   : noFilesFound
    Created on : 20-May-2016, 17:32:15
    Author     : vplep
--%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">

        <!--[if lt IE 9]>
        <script src="http://html5shiv.googlecode.com/svn/trunk/html5.js">
        </script>
        <![endif]-->

        <style>


            html
            {
                width: 100%;
                height: 100%;
            }

            body
            {
                background-image: url(background.png);
                background-size: cover;
                display: flex;
                justify-content: center;
                align-items: center;
            }

            p
            {
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
            }
            
        </style>

    </head>
    <body> 
        <p> <font color="red">No data files with a *.csv extension were found by this software. It is either because a wrong directory was specified, or 
            there are no data files with a *.csv extension in it. You can specify the directory with data files in the DataFeed.java servlet.</font></p>
    </body>
</html>
