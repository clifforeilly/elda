
import au.com.bytecode.opencsv.CSVReader;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by cliff on 31/05/2014.
 */

//args
// 0=input folder
// 1=output folder
// 2=lda exe folder
// 3=type
// 4=binary for delete content of output folder before starting parse
// 5=LabelsColumn
// 6=DocColumn
// 7=osType
// 8=TermSmoothing



public class elda
{
    static String InputFileFolder = "";
    static String OutputFileFolder = "";
    static String LDAExeFolder = "";
    static int type = 0;
    static int LabelsColumn;
    static int DocColumn;
    static String osType;
    static String termSmoothing;

    public static void main(String[] args)
    {
        try
        {
            if(args.length>0)
            {
                System.out.println(args[0]);
                InputFileFolder=args[0];
                System.out.println(args[1]);
                OutputFileFolder=args[1];

                System.out.println(args[2]);
                LDAExeFolder=args[2];
                System.out.println(args[3]);
                type=Integer.parseInt(args[3]);

                if(Integer.parseInt(args[4])==1)      //delete the output before parsing
                {
                    File f = new File(OutputFileFolder);
                    File[] matchingFiles = f.listFiles();

                    if(matchingFiles!=null)
                    {
                        for(File tf : matchingFiles)
                        {
                            tf.delete();
                        }
                    }
                }

                System.out.println(args[5]);
                LabelsColumn=Integer.parseInt(args[5]);
                System.out.println(args[6]);
                DocColumn=Integer.parseInt(args[6]);
                System.out.println(args[7]);
                osType=args[7];
                String slash;

                if(osType.equals("Mac"))
                {
                    slash="/";
                }
                else
                {
                    slash="\\";
                }

                System.out.println(args[8]);
                termSmoothing=args[8];

                if (type == 1)
                {
                    List itsimport;
                    String cmd="";

                    //load its file
                    String itsfile = InputFileFolder + slash + "its.csv";
                    CSVReader itsdata = new CSVReader(new FileReader(itsfile));
                    itsimport = itsdata.readAll();
                    itsdata.close();
                    String[] rowits;

                    OutputFileFolder = OutputFileFolder + slash + "ts0.5";

                    int[] colvals = new int[23];
                    colvals[0]=0;
                    colvals[1]=6;
                    colvals[2]=7;
                    colvals[3]=10;
                    colvals[4]=11;
                    colvals[5]=13;
                    colvals[6]=14;
                    colvals[7]=15;
                    colvals[8]=16;
                    colvals[9]=17;
                    colvals[10]=18;
                    colvals[11]=19;
                    colvals[12]=20;
                    colvals[13]=21;
                    colvals[14]=22;
                    colvals[15]=23;
                    colvals[16]=24;
                    colvals[17]=25;
                    colvals[18]=26;
                    colvals[19]=27;
                    colvals[20]=28;
                    colvals[21]=29;
                    colvals[22]=30;


                    for(int w : colvals)
                    {
                        int thisColVal = LabelsColumn + w;
                        int c = 0;
                        for(Object ob : itsimport)
                        {
                            c++;
                            int thisFolderVal = thisColVal;
                            rowits=(String[]) ob;

                            //llda train
                            cmd="java -jar \"" + LDAExeFolder + slash + "tmt-0.4.0.jar\" \"" + LDAExeFolder + slash + "1-llda-learn.scala" + "\" \"" + rowits[1] + "\" \"" + thisColVal + "\" \"" + DocColumn + "\" \"" + OutputFileFolder + slash + "c" + thisFolderVal + slash + "it" + c + "\" \"" + termSmoothing + "\"";

                            System.out.println(cmd);
                            try
                            {
                                Process p = Runtime.getRuntime().exec(cmd);
                                InputStream is = p.getErrorStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader br = new BufferedReader(isr);
                                String line = null;
                                System.out.println("<llda-learn>");
                                while ((line=br.readLine())!=null)
                                {
                                    System.out.println(line);
                                }
                                System.out.println("</llda-learn>");
                                int exitVal = p.waitFor();
                                System.out.println("Process exitValue: " + exitVal);

                            }
                            catch(Throwable t)
                            {
                                t.printStackTrace();
                            }

                            //llda infer on training data
                            cmd="java -jar \"" + LDAExeFolder + slash + "tmt-0.4.0.jar\" \"" + LDAExeFolder + slash + "2-llda-infer-train.scala" + "\" \"" + rowits[1] + "\" \"" + thisColVal + "\" \"" + DocColumn + "\" \"" + OutputFileFolder + slash + "c" + thisFolderVal + slash + "it" + c + "\" \"" + OutputFileFolder + slash + "c" + thisFolderVal + slash + "it" + c + "\"";
                            System.out.println(cmd);
                            try
                            {
                                Process p = Runtime.getRuntime().exec(cmd);
                                InputStream is = p.getErrorStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader br = new BufferedReader(isr);
                                String line = null;
                                System.out.println("<llda-infer-1>");
                                while ((line=br.readLine())!=null)
                                {
                                    System.out.println(line);
                                }
                                System.out.println("</llda-infer-1>");
                                int exitVal = p.waitFor();
                                System.out.println("Process exitValue: " + exitVal);

                            }
                            catch(Throwable t)
                            {
                                t.printStackTrace();
                            }


                            //llda infer on test
                            cmd="java -jar \"" + LDAExeFolder + slash + "tmt-0.4.0.jar\" \"" + LDAExeFolder + slash + "2-llda-infer.scala" + "\" \"" + rowits[2] + "\" \"" + thisColVal + "\" \"" + DocColumn + "\" \"" + OutputFileFolder + slash + "c" + thisFolderVal + slash + "it" + c + "\" " + OutputFileFolder + slash + "c" + thisFolderVal + slash + "it" + c + "\"";
                            System.out.println(cmd);
                            try
                            {

                                Process p = Runtime.getRuntime().exec(cmd);
                                InputStream is = p.getErrorStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader br = new BufferedReader(isr);
                                String line = null;
                                System.out.println("<llda-infer-2>");
                                while ((line=br.readLine())!=null)
                                {
                                    System.out.println(line);
                                }
                                System.out.println("</llda-infer-2>");
                                int exitVal = p.waitFor();
                                System.out.println("Process exitValue: " + exitVal);

                            }
                            catch(Throwable t)
                            {
                                t.printStackTrace();
                            }
                        }
                    }
                } //if (type == 1)


                if (type == 2)
                {
                    File f = new File(InputFileFolder);
                    File[] matchingFiles = f.listFiles(new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.toLowerCase().endsWith(".csv");
                        }
                    });
                    System.out.println("Scanning ... " + InputFileFolder);
                    int filecount=0;
                    String cmd="";

                    int DifferentFileFactor = 10; // use this is the columns are different

                    for(File tf : matchingFiles)
                    {
                        filecount++;

                        for (int col = 10 ; col < 32 ; col++)
                        {
                            int thisCol = DocColumn + col - DifferentFileFactor;
                            cmd="java -jar \"" + LDAExeFolder + slash + "tmt-0.4.0.jar\" \"" + LDAExeFolder + slash + "1-lda-learn.scala" + "\" \"" + tf.getName() + "\" \"" + thisCol + "\" \"" + OutputFileFolder + "\" \"" + InputFileFolder + "\"";

                            System.out.println(cmd);

                            try
                            {
                                Process p = Runtime.getRuntime().exec(cmd);
                                InputStream is = p.getErrorStream();
                                InputStreamReader isr = new InputStreamReader(is);
                                BufferedReader br = new BufferedReader(isr);
                                String line = null;
                                System.out.println("<lda-learn>");
                                while ((line=br.readLine())!=null)
                                {
                                    System.out.println(line);
                                }
                                System.out.println("</lda-learn>");
                                int exitVal = p.waitFor();
                                System.out.println("Process exitValue: " + exitVal);

                            }
                            catch(Throwable t)
                            {
                                t.printStackTrace();
                            }
                        }

                    }


                } //if (type == 2)

            }
            else
            {
                System.out.println("Error - no parameters supplied");
            }
        }
        catch (Exception ex)
        {
            System.out.println("Error:-" + ex.toString() + ", " + ex.getMessage() + ", " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }
}
