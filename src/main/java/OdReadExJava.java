/////////////////////////////////////////////////////////////////////////////// 
// Copyright (C) 2003-2014, Open Design Alliance (the "Alliance"). 
// All rights reserved. 
// 
// This software and its documentation and related materials are owned by 
// the Alliance. The software may only be incorporated into application 
// programs owned by members of the Alliance, subject to a signed 
// Membership Agreement and Supplemental Software License Agreement with the
// Alliance. The structure and organization of this software are the valuable  
// trade secrets of the Alliance and its suppliers. The software is also 
// protected by copyright law and international treaty provisions. Application  
// programs incorporating this software must include the following statement 
// with their copyright notices:
//   
//   This application incorporates Teigha(R) software pursuant to a license 
//   agreement with Open Design Alliance.
//   Teigha(R) Copyright (C) 2003-2014 by Open Design Alliance. 
//   All rights reserved.
//
// By use of this software, its documentation or related materials, you 
// acknowledge and accept the above terms.
///////////////////////////////////////////////////////////////////////////////
// This example illustrates how C++ classes can be used from Java using SWIG.
// The Java class gets mapped onto the C++ class and behaves as if it is a Java class.

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;

import com.opendesign.core.ExSystemServices;
import com.opendesign.core.Globals;
import com.opendesign.core.OdCmEntityColor;
import com.opendesign.core.OdDbHandle;
import com.opendesign.core.OdGeCircArc2d;
import com.opendesign.core.OdGeCurve2d;
import com.opendesign.core.OdGeDoubleArray;
import com.opendesign.core.OdGeEllipArc2d;
import com.opendesign.core.OdGeInterval;
import com.opendesign.core.OdGeKnotVector;
import com.opendesign.core.OdGeNurbCurve2d;
import com.opendesign.core.OdGePoint2d;
import com.opendesign.core.OdGePoint2dArray;
import com.opendesign.core.OdGeScale3d;
import com.opendesign.core.OdGeVector2d;
import com.opendesign.core.OdRxClass;
import com.opendesign.core.LineWeight;
import com.opendesign.core.OdGeVector3d;
import com.opendesign.core.OdGeExtents3d;
import com.opendesign.core.OdGePoint3d;
import com.opendesign.core.OdGePoint3dArray;
import com.opendesign.core.OdResult;
import com.opendesign.core.OdCmTransparency;
import com.opendesign.core.DwgVersion;
import com.opendesign.core.OdGe;
import com.opendesign.core.OdGiWorldDraw;
import com.opendesign.core.*;
import com.opendesign.td.*;
import com.opendesign.td.OdDbHatch.HatchLoopType;
import com.opendesign.td.OdDbHatch.HatchObjectType;
import com.opendesign.td.OdDbHatch.HatchPatternType;
import com.opendesign.td.OdDbHatch.HatchStyle;
import com.opendesign.td.OdDbMText.AttachmentPoint;
import com.opendesign.td.OdDbMText.FlowDirection;
import com.opendesign.td.OdDxfCode.Type;


public class OdReadExJava {

    public static File file;
    public static PrintWriter writer;

    static {
        try {
            System.loadLibrary("TD_ALLOC_4.00_10");
            System.loadLibrary("TD_ROOT_4.00_10");
            System.loadLibrary("SISL");
            System.loadLibrary("TD_GE_4.00_10");
            System.loadLibrary("TD_SpatialIndex_4.00_10");
            System.loadLibrary("TD_Gi_4.00_10");
            System.loadLibrary("TD_Gs_4.00_10");
            System.loadLibrary("TD_DBROOT_4.00_10");
            System.loadLibrary("TD_Br_4.00_10");
            System.loadLibrary("TD_DB_4.00_10");
            System.loadLibrary("TeighaJavaCore");
            System.loadLibrary("TeighaJavaDwg");
        } catch (UnsatisfiedLinkError e) {
            System.out.println("Native code library failed to load. See the chapter on Dynamic Linking Problems in the SWIG Java documentation for help.\n" + e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String argv[]) {
        System.out.println("\nTeighaJava sample program. Copyright (c) 2012, Open Design Alliance\n");

        /**********************************************************************/
            /* Parse Command Line inputs                                          */
        /**********************************************************************/
//        if (argv.length < 2) {
//            System.out.println("Usage: OdReadEx <srcfilename> <dstfilename> (<pdffilename>)");
//            return;
//        }
//        boolean expPDF = false;
//        if (argv.length == 3) // export to pdf
//        {
//            expPDF = true;
//        }

        file = new File("dump");

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            writer = new PrintWriter(file.getAbsoluteFile(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

        /********************************************************************/
            /* Create a Service and HostApp instances.                          */
        /********************************************************************/
        ExSystemServices systemServices = new ExSystemServices();
        ExHostAppServices hostApp = new ExHostAppServices();
        hostApp.disableOutput(true); //Disable progress meter

        /********************************************************************/
			/* Initialize Teigha.                                               */
        /********************************************************************/
        TD_Db.odInitialize(systemServices);


//			String srcFileName = argv[0];
//            String dstFileName = argv[1];
//            String pdfFileName = "";
//            if (expPDF)
//            {
//              pdfFileName = argv[2];
//            }

        /********************************************************************/
			/* Display the Product and Version that created the executable      */
        /********************************************************************/
        System.out.format("\nOdReadEx developed using %s ver %s\n", hostApp.product(), hostApp.versionString());

        boolean success = true;
        try {
            /******************************************************************/
				/* Create a database and load the drawing into it.                */
            /******************************************************************/
            OdDbDatabase db = null;

//				if (!hostApp.findFile(srcFileName).isEmpty())
//				{
//					db = hostApp.readFile(srcFileName);
//				}

            if (!hostApp.findFile("KAQ-0803.dwg").isEmpty()) {
                db = hostApp.readFile("KAQ-0803.dwg");
            }


            if (db != null) {
//                if (expPDF) {
//                    System.out.format("\nOdReadEx export to pdf file %s\n", pdfFileName);
//                    // PDF start
//                    OdRxModule pdfModule = Globals.odrxDynamicLinker().loadApp("TD_PdfExport");
//
//                    PDFExportParams exportParams = new PDFExportParams();
//                    // added string props start
//                    exportParams.setTitle("OdReadEx_java");
//                    exportParams.setAuthor("OdReadEx_java");
//                    exportParams.setSubject("OdReadEx_java");
//                    exportParams.setKeywords("OdReadEx_java");
//                    exportParams.setCreator("OdReadEx_java");
//                    exportParams.setProducer("OdReadEx_java");
//                    // added string props stop
//                    exportParams.setVer(PDFExportParams.PDFExportVersions.kPDFv1_5);
//                    exportParams.setBFlateCompression(true);
//                    exportParams.setBUseHLR(true);
//                    exportParams.setBASCIIHexEncoding(true);
//                    exportParams.setBAllViews(false);
//
//                    exportParams.setFlags(PDFExportParams.PDFExportFlags.kEmbededTTF |
//                            PDFExportParams.PDFExportFlags.kSHXTextAsGeometry |
//                            PDFExportParams.PDFExportFlags.kSimpleGeomOptimization |
//                            PDFExportParams.PDFExportFlags.kZoomToExtentsMode |
//                            PDFExportParams.PDFExportFlags.kEnableLayers |
//                            PDFExportParams.PDFExportFlags.kIncludeOffLayers);
//
//                    exportParams.setPDb(db);
//                    long[] CurPalette = Globals.odcmAcadLightPalette();
//                    CurPalette[255] = 0x00ffffff;
//                    exportParams.setPPalette(CurPalette);
//
//                    OdStreamBuf file = systemServices.createFile(pdfFileName, FileAccessMode.kFileWrite, FileShareMode.kShareDenyNo, FileCreationDisposition.kCreateAlways);
//                    exportParams.setOutputStream(file);
//
//                    // set layout - it is possible to use actuve only
//                    OdStringArray layArr = exportParams.getLayouts();
//                    layArr.add(db.findActiveLayout(true));
//                    exportParams.setLayouts(layArr);
//
//                    OdGsPageParamsArray ppArr = exportParams.getPageParams();
//                    long len = layArr.size();
//                    if (1 > layArr.size()) len = 1;
//                    ppArr.resize(len);
//                    exportParams.setPageParams(ppArr);
//
//                    PdfExportModule module = new PdfExportModule(OdRxModule.getCPtr(pdfModule), false); //= PdfExportModule.cast();
//                    OdPdfExport exporter = module.create();
//
//                    long errCode = exporter.exportPdf(exportParams);
//                    if (errCode != 0) {
//                        System.out.format("\nError code: %d\n", errCode);
//                        String errMsg = exporter.exportPdfErrorCode(errCode);
//                        System.out.println(errMsg);
//                    }
//                    file.delete();
//                    exportParams.delete();
//                    pdfModule.delete();
//                    // PDF stop
//                    System.out.format("\nOdReadEx export to pdf finished\n");
//                }

                /****************************************************************/
					/* Display the File Version                                     */
                /****************************************************************/
                System.out.format("\nFile Version: %d\n", db.originalFileVersion().swigValue());

                /****************************************************************/
					/* Dump the database                                            */
                /****************************************************************/
                DbDumper dumper = new DbDumper();
                dumper.dump(db);
                System.out.println("Dumping finished\n");
                writer.println("Dumping finished\n");
                writer.close();
            }

            /********************************************************************/
                /* Write the OdDgDatabase data into the DGN file                    */
            /********************************************************************/
//            if (null != db) {
//                System.out.println("write database to file");
//                db.writeFile(dstFileName, SaveType.kDwg, DwgVersion.vAC12, true);
//            } else {
//                System.out.println("db is null");
//            }

        }
        /********************************************************************/
            /* Display the error                                                */
        /********************************************************************/ catch (Error Err) {
            System.out.println("Error2" + Err);
        }
        hostApp.delete();

        TD_Db.odUninitialize();
        System.out.println("Teigha uninitialized\n");
    }


    private static class DbDumper {

        public void dump(OdDbDatabase db) {
            int indent = 0;
            System.out.println("\n");
//            System.out.println("<<dumpHeader start>>\n");
//            dumpHeader(db, indent);
            //dumpDictionaries(db, indent);
            //dumpLayers(db, indent);
//            System.out.println("<<dumpLayers1 start>>\n");
//            dumpLayers1(db, indent);
            System.out.println("<<dumpLinetypes start>>\n");
            //dumpLinetypes(db, indent);
//            System.out.println("<<dumpTextStyles start>>\n");
//            dumpTextStyles(db, indent);
//            System.out.println("<<dumpDimStyles start>>\n");
//            dumpDimStyles(db, indent);
//            System.out.println("<<dumpRegApps start>>\n");
//            dumpRegApps(db, indent);
//            System.out.println("<<dumpViewports start>>\n");
//            dumpViewports(db, indent);
//            System.out.println("<<dumpViews1 start>>\n");
//            dumpViews1(db, indent);
//            System.out.println("<<dumpMLineStyles start>>\n");
//            dumpMLineStyles(db, indent);
//            System.out.println("<<dumpUCSTable start>>\n");
//            dumpUCSTable(db, indent);
//            System.out.println("<<dumpObject start>>\n");
//            dumpObject(db.getNamedObjectsDictionaryId(), "Named Objects Dictionary", indent);
//            System.out.println("<<dumpBlocks start>>\n");
//            dumpBlocks(db, indent);

            dumpSupportedBlocks(db, indent);
        }

        private void dumpHeader(OdDbDatabase pDb, int indent) {
            writeLine();
            writeLine(indent, "Filename: = ", String.valueOf(pDb.getFilename()));
            writeLine(indent, "File DWG Version: = ", String.valueOf(pDb.originalFileVersion()));

            writeLine();
            writeLine(indent, "Header Variables:");

            writeLine();
            writeLine(indent, "TDCREATE: = ", String.valueOf(pDb.getTDCREATE()));
            writeLine(indent, "TDUPDATE: = ", String.valueOf(pDb.getTDUPDATE()));

            writeLine();
            writeLine(indent, "ANGBASE = ", String.valueOf(pDb.getANGBASE()));
            writeLine(indent, "ANGDIR = ", String.valueOf(pDb.getANGDIR()));
            writeLine(indent, "ATTMODE = ", String.valueOf(pDb.getATTMODE()));
            writeLine(indent, "AUNITS = ", String.valueOf(pDb.getAUNITS()));
            writeLine(indent, "AUPREC = ", String.valueOf(pDb.getAUPREC()));
            writeLine(indent, "CECOLOR = ", String.valueOf(pDb.getCECOLOR()));
            writeLine(indent, "CELTSCALE = ", String.valueOf(pDb.getCELTSCALE()));
            writeLine(indent, "CHAMFERA = ", String.valueOf(pDb.getCHAMFERA()));
            writeLine(indent, "CHAMFERB = ", String.valueOf(pDb.getCHAMFERB()));
            writeLine(indent, "CHAMFERC = ", String.valueOf(pDb.getCHAMFERC()));
            writeLine(indent, "CHAMFERD = ", String.valueOf(pDb.getCHAMFERD()));
            writeLine(indent, "CMLJUST = ", String.valueOf(pDb.getCMLJUST()));
            writeLine(indent, "CMLSCALE = ", String.valueOf(pDb.getCMLSCALE()));
            writeLine(indent, "DIMADEC = ", String.valueOf(pDb.dimadec()));
            writeLine(indent, "DIMALT = ", String.valueOf(pDb.dimalt()));
            writeLine(indent, "DIMALTD = ", String.valueOf(pDb.dimaltd()));
            writeLine(indent, "DIMALTF = ", String.valueOf(pDb.dimaltf()));
            writeLine(indent, "DIMALTRND = ", String.valueOf(pDb.dimaltrnd()));
            writeLine(indent, "DIMALTTD = ", String.valueOf(pDb.dimalttd()));
            writeLine(indent, "DIMALTTZ = ", String.valueOf(pDb.dimalttz()));
            writeLine(indent, "DIMALTU = ", String.valueOf(pDb.dimaltu()));
            writeLine(indent, "DIMALTZ = ", String.valueOf(pDb.dimaltz()));
            writeLine(indent, "DIMAPOST = ", String.valueOf(pDb.dimapost()));
            writeLine(indent, "DIMASZ = ", String.valueOf(pDb.dimasz()));
            writeLine(indent, "DIMATFIT = ", String.valueOf(pDb.dimatfit()));
            writeLine(indent, "DIMAUNIT = ", String.valueOf(pDb.dimaunit()));
            writeLine(indent, "DIMAZIN = ", String.valueOf(pDb.dimazin()));
            writeLine(indent, "DIMBLK = ", String.valueOf(pDb.dimblk()));
            writeLine(indent, "DIMBLK1 = ", String.valueOf(pDb.dimblk1()));
            writeLine(indent, "DIMBLK2 = ", String.valueOf(pDb.dimblk2()));
            writeLine(indent, "DIMCEN = ", String.valueOf(pDb.dimcen()));
            writeLine(indent, "DIMCLRD = ", String.valueOf(pDb.dimclrd()));
            writeLine(indent, "DIMCLRE = ", String.valueOf(pDb.dimclre()));
            writeLine(indent, "DIMCLRT = ", String.valueOf(pDb.dimclrt()));
            writeLine(indent, "DIMDEC = ", String.valueOf(pDb.dimdec()));
            writeLine(indent, "DIMDLE = ", String.valueOf(pDb.dimdle()));
            writeLine(indent, "DIMDLI = ", String.valueOf(pDb.dimdli()));
            writeLine(indent, "DIMDSEP = ", String.valueOf(pDb.dimdsep()));
            writeLine(indent, "DIMEXE = ", String.valueOf(pDb.dimexe()));
            writeLine(indent, "DIMEXO = ", String.valueOf(pDb.dimexo()));
            writeLine(indent, "DIMFRAC = ", String.valueOf(pDb.dimfrac()));
            writeLine(indent, "DIMGAP = ", String.valueOf(pDb.dimgap()));
            writeLine(indent, "DIMJUST = ", String.valueOf(pDb.dimjust()));
            writeLine(indent, "DIMLDRBLK = ", String.valueOf(pDb.dimldrblk()));
            writeLine(indent, "DIMLFAC = ", String.valueOf(pDb.dimlfac()));
            writeLine(indent, "DIMLIM = ", String.valueOf(pDb.dimlim()));
            writeLine(indent, "DIMLUNIT = ", String.valueOf(pDb.dimlunit()));
            writeLine(indent, "DIMLWD = ", String.valueOf(pDb.dimlwd()));
            writeLine(indent, "DIMLWE = ", String.valueOf(pDb.dimlwe()));
            writeLine(indent, "DIMPOST = ", String.valueOf(pDb.dimpost()));
            writeLine(indent, "DIMRND = ", String.valueOf(pDb.dimrnd()));
            writeLine(indent, "DIMSAH = ", String.valueOf(pDb.dimsah()));
            writeLine(indent, "DIMSCALE = ", String.valueOf(pDb.dimscale()));
            writeLine(indent, "DIMSD1 = ", String.valueOf(pDb.dimsd1()));
            writeLine(indent, "DIMSD2 = ", String.valueOf(pDb.dimsd2()));
            writeLine(indent, "DIMSE1 = ", String.valueOf(pDb.dimse1()));
            writeLine(indent, "DIMSE2 = ", String.valueOf(pDb.dimse2()));
            writeLine(indent, "DIMSOXD = ", String.valueOf(pDb.dimsoxd()));
            writeLine(indent, "DIMTAD = ", String.valueOf(pDb.dimtad()));
            writeLine(indent, "DIMTDEC = ", String.valueOf(pDb.dimtdec()));
            writeLine(indent, "DIMTFAC = ", String.valueOf(pDb.dimtfac()));
            writeLine(indent, "DIMTIH = ", String.valueOf(pDb.dimtih()));
            writeLine(indent, "DIMTIX = ", String.valueOf(pDb.dimtix()));
            writeLine(indent, "DIMTM = ", String.valueOf(pDb.dimtm()));
            writeLine(indent, "DIMTOFL = ", String.valueOf(pDb.dimtofl()));
            writeLine(indent, "DIMTOH = ", String.valueOf(pDb.dimtoh()));
            writeLine(indent, "DIMTOL = ", String.valueOf(pDb.dimtol()));
            writeLine(indent, "DIMTOLJ = ", String.valueOf(pDb.dimtolj()));
            writeLine(indent, "DIMTP = ", String.valueOf(pDb.dimtp()));
            writeLine(indent, "DIMTSZ = ", String.valueOf(pDb.dimtsz()));
            writeLine(indent, "DIMTVP = ", String.valueOf(pDb.dimtvp()));
            writeLine(indent, "DIMTXSTY = ", String.valueOf(pDb.dimtxsty()));
            writeLine(indent, "DIMTXT = ", String.valueOf(pDb.dimtxt()));
            writeLine(indent, "DIMTZIN = ", String.valueOf(pDb.dimtzin()));
            writeLine(indent, "DIMUPT = ", String.valueOf(pDb.dimupt()));
            writeLine(indent, "DIMZIN = ", String.valueOf(pDb.dimzin()));
            writeLine(indent, "DISPSILH = ", String.valueOf(pDb.getDISPSILH()));
            writeLine(indent, "DRAWORDERCTL = ", String.valueOf(pDb.getDRAWORDERCTL()));
            writeLine(indent, "ELEVATION = ", String.valueOf(pDb.getELEVATION()));
            writeLine(indent, "EXTMAX = ", String.valueOf(pDb.getEXTMAX()));
            writeLine(indent, "EXTMIN = ", String.valueOf(pDb.getEXTMIN()));
            writeLine(indent, "FACETRES = ", String.valueOf(pDb.getFACETRES()));
            writeLine(indent, "FILLETRAD = ", String.valueOf(pDb.getFILLETRAD()));
            writeLine(indent, "FILLMODE = ", String.valueOf(pDb.getFILLMODE()));
            writeLine(indent, "INSBASE = ", String.valueOf(pDb.getINSBASE()));
            writeLine(indent, "ISOLINES = ", String.valueOf(pDb.getISOLINES()));
            writeLine(indent, "LIMCHECK = ", String.valueOf(pDb.getLIMCHECK()));
            writeLine(indent, "LIMMAX = ", String.valueOf(pDb.getLIMMAX()));
            writeLine(indent, "LIMMIN = ", String.valueOf(pDb.getLIMMIN()));
            writeLine(indent, "LTSCALE = ", String.valueOf(pDb.getLTSCALE()));
            writeLine(indent, "LUNITS = ", String.valueOf(pDb.getLUNITS()));
            writeLine(indent, "LUPREC = ", String.valueOf(pDb.getLUPREC()));
            writeLine(indent, "MAXACTVP = ", String.valueOf(pDb.getMAXACTVP()));
            writeLine(indent, "MIRRTEXT = ", String.valueOf(pDb.getMIRRTEXT()));
            writeLine(indent, "ORTHOMODE = ", String.valueOf(pDb.getORTHOMODE()));
            writeLine(indent, "PDMODE = ", String.valueOf(pDb.getPDMODE()));
            writeLine(indent, "PDSIZE = ", String.valueOf(pDb.getPDSIZE()));
            writeLine(indent, "PELEVATION = ", String.valueOf(pDb.getPELEVATION()));
            writeLine(indent, "PELLIPSE = ", String.valueOf(pDb.getPELLIPSE()));
            writeLine(indent, "PEXTMAX = ", String.valueOf(pDb.getPEXTMAX()));
            writeLine(indent, "PEXTMIN = ", String.valueOf(pDb.getPEXTMIN()));
            writeLine(indent, "PINSBASE = ", String.valueOf(pDb.getPINSBASE()));
            writeLine(indent, "PLIMCHECK = ", String.valueOf(pDb.getPLIMCHECK()));
            writeLine(indent, "PLIMMAX = ", String.valueOf(pDb.getPLIMMAX()));
            writeLine(indent, "PLIMMIN = ", String.valueOf(pDb.getPLIMMIN()));
            writeLine(indent, "PLINEGEN = ", String.valueOf(pDb.getPLINEGEN()));
            writeLine(indent, "PLINEWID = ", String.valueOf(pDb.getPLINEWID()));
            writeLine(indent, "PROXYGRAPHICS = ", String.valueOf(pDb.getPROXYGRAPHICS()));
            writeLine(indent, "PSLTSCALE = ", String.valueOf(pDb.getPSLTSCALE()));
            writeLine(indent, "PUCSNAME = ", String.valueOf(pDb.getPUCSNAME()));
            writeLine(indent, "PUCSORG = ", String.valueOf(pDb.getPUCSORG()));
            writeLine(indent, "PUCSXDIR = ", String.valueOf(pDb.getPUCSXDIR()));
            writeLine(indent, "PUCSYDIR = ", String.valueOf(pDb.getPUCSYDIR()));
            writeLine(indent, "QTEXTMODE = ", String.valueOf(pDb.getQTEXTMODE()));
            writeLine(indent, "REGENMODE = ", String.valueOf(pDb.getREGENMODE()));
            writeLine(indent, "SHADEDGE = ", String.valueOf(pDb.getSHADEDGE()));
            writeLine(indent, "SHADEDIF = ", String.valueOf(pDb.getSHADEDIF()));
            writeLine(indent, "SKETCHINC = ", String.valueOf(pDb.getSKETCHINC()));
            writeLine(indent, "SKPOLY = ", String.valueOf(pDb.getSKPOLY()));
            writeLine(indent, "SPLFRAME = ", String.valueOf(pDb.getSPLFRAME()));
            writeLine(indent, "SPLINESEGS = ", String.valueOf(pDb.getSPLINESEGS()));
            writeLine(indent, "SPLINETYPE = ", String.valueOf(pDb.getSPLINETYPE()));
            writeLine(indent, "SURFTAB1 = ", String.valueOf(pDb.getSURFTAB1()));
            writeLine(indent, "SURFTAB2 = ", String.valueOf(pDb.getSURFTAB2()));
            writeLine(indent, "SURFTYPE = ", String.valueOf(pDb.getSURFTYPE()));
            writeLine(indent, "SURFU = ", String.valueOf(pDb.getSURFU()));
            writeLine(indent, "SURFV = ", String.valueOf(pDb.getSURFV()));
            writeLine(indent, "TEXTQLTY = ", String.valueOf(pDb.getTEXTQLTY()));
            writeLine(indent, "TEXTSIZE = ", String.valueOf(pDb.getTEXTSIZE()));
            writeLine(indent, "THICKNESS = ", String.valueOf(pDb.getTHICKNESS()));
            writeLine(indent, "TILEMODE = ", String.valueOf(pDb.getTILEMODE()));
            writeLine(indent, "TRACEWID = ", String.valueOf(pDb.getTRACEWID()));
            writeLine(indent, "TREEDEPTH = ", String.valueOf(pDb.getTREEDEPTH()));
            writeLine(indent, "UCSNAME = ", String.valueOf(pDb.getUCSNAME()));
            writeLine(indent, "UCSORG = ", String.valueOf(pDb.getUCSORG()));
            writeLine(indent, "UCSXDIR = ", String.valueOf(pDb.getUCSXDIR()));
            writeLine(indent, "UCSYDIR = ", String.valueOf(pDb.getUCSYDIR()));
            writeLine(indent, "UNITMODE = ", String.valueOf(pDb.getUNITMODE()));
            writeLine(indent, "USERI1 = ", String.valueOf(pDb.getUSERI1()));
            writeLine(indent, "USERI2 = ", String.valueOf(pDb.getUSERI2()));
            writeLine(indent, "USERI3 = ", String.valueOf(pDb.getUSERI3()));
            writeLine(indent, "USERI4 = ", String.valueOf(pDb.getUSERI4()));
            writeLine(indent, "USERI5 = ", String.valueOf(pDb.getUSERI5()));
            writeLine(indent, "USERR1 = ", String.valueOf(pDb.getUSERR1()));
            writeLine(indent, "USERR2 = ", String.valueOf(pDb.getUSERR2()));
            writeLine(indent, "USERR3 = ", String.valueOf(pDb.getUSERR3()));
            writeLine(indent, "USERR4 = ", String.valueOf(pDb.getUSERR4()));
            writeLine(indent, "USERR5 = ", String.valueOf(pDb.getUSERR5()));
            writeLine(indent, "USRTIMER = ", String.valueOf(pDb.getUSRTIMER()));
            writeLine(indent, "VISRETAIN = ", String.valueOf(pDb.getVISRETAIN()));
            writeLine(indent, "WORLDVIEW = ", String.valueOf(pDb.getWORLDVIEW()));
        }
  /*
	private void dumpColors(OdDbDatabase db, int indent) {
		
		OdDbDictionary colors =OdDbDictionary.cast(db.getColorDictionaryId(true).safeOpenObject());
		OdDbDictionaryIteratorPtr itColor;
		  OdDbColorPtr pColor;

		 for(itColor = pColors->newIterator() ; !itColor->done() ; itColor->next())
	           wcout << L"\"" << itColor->name() << L"\"\n";
	        break;

		
	}
*/

        private void dumpSysVars(OdDbDatabase db, int indent) {
            writeLine();

            writeLine(indent, "ANGBASE", toString(db.getSysVar("ANGBASE")));
            writeLine(indent, "ANGDIR", toString(db.getSysVar("ANGDIR")));

            writeLine(indent, "INSBASE", toString(db.getSysVar("INSBASE")));
            writeLine(indent, "EXTMIN", toString(db.getSysVar("EXTMIN")));
            writeLine(indent, "EXTMAX", toString(db.getSysVar("EXTMAX")));

            writeLine(indent, "LIMMIN", toString(db.getSysVar("LIMMIN")));
            writeLine(indent, "LIMMAX", toString(db.getSysVar("LIMMAX")));
            writeLine(indent, "ORTHOMODE", toString(db.getSysVar("ORTHOMODE")));
            writeLine(indent, "REGENMODE", toString(db.getSysVar("REGENMODE")));

            writeLine(indent, "FILLMODE", toString(db.getSysVar("FILLMODE")));
            writeLine(indent, "QTEXTMODE", toString(db.getSysVar("QTEXTMODE")));
            writeLine(indent, "MIRRTEXT", toString(db.getSysVar("MIRRTEXT")));
            writeLine(indent, "LTSCALE", toString(db.getSysVar("LTSCALE")));
            writeLine(indent, "ATTMODE", toString(db.getSysVar("ATTMODE")));

            writeLine(indent, "TEXTSIZE", toString(db.getSysVar("TEXTSIZE")));
            writeLine(indent, "TRACEWID", toString(db.getSysVar("TRACEWID")));
            writeLine(indent, "TEXTSTYLE", toString(db.getSysVar("TEXTSTYLE")));
            writeLine(indent, "CLAYER", toString(db.getSysVar("CLAYER")));
            writeLine(indent, "CELTYPE", toString(db.getSysVar("CELTYPE")));

            writeLine(indent, "CECOLOR", toString(db.getSysVar("CECOLOR")));
            writeLine(indent, "CELTSCALE", toString(db.getSysVar("CELTSCALE")));
            writeLine(indent, "CHAMFERA", toString(db.getSysVar("CHAMFERA")));
            writeLine(indent, "CHAMFERB", toString(db.getSysVar("CHAMFERB")));
            writeLine(indent, "CHAMFERC", toString(db.getSysVar("CHAMFERC")));
            writeLine(indent, "CHAMFERD", toString(db.getSysVar("CHAMFERD")));
            writeLine(indent, "DISPSILH", toString(db.getSysVar("DISPSILH")));
            writeLine(indent, "DIMSTYLE", toString(db.getSysVar("DIMSTYLE")));

            writeLine(indent, "DIMASO", toString(db.getSysVar("DIMASO")));

            writeLine(indent, "LUNITS", toString(db.getSysVar("LUNITS")));
            writeLine(indent, "LUPREC", toString(db.getSysVar("LUPREC")));
            writeLine(indent, "SKETCHINC", toString(db.getSysVar("SKETCHINC")));
            writeLine(indent, "FILLETRAD", toString(db.getSysVar("FILLETRAD")));
            writeLine(indent, "AUNITS", toString(db.getSysVar("AUNITS")));

            writeLine(indent, "AUPREC", toString(db.getSysVar("AUPREC")));


            writeLine(indent, "INSUNITS", toString(db.getSysVar("INSUNITS")));


        }

        private String toString(OdResBuf sysVar) {
            Type type = OdDxfCode._getType(sysVar.restype());
            switch (type) {
                case Unknown:
                    return "Unknown";
                case Name:
                    return "Name:??";
                case String:
                    return "String: " + sysVar.getString();
                case Bool:
                    return "Bool: " + (sysVar.getBool() ? "true" : "false");
                case Integer8:
                    return "Integer8: " + sysVar.getInt8();
                case Integer16:
                    return "Integer16: " + sysVar.getInt16();
                case Integer32:
                    return "Integer32: " + sysVar.getInt32();
                case Double:
                    return "Double: " + sysVar.getDouble();
                case Angle:
                    return "Angle:??";
                case Point:
                    return "Point: " + _toString(sysVar.getPoint3d());
                case BinaryChunk:
                    return "BinaryChunk:??";
                case LayerName:
                    return "LayerName:??";
                case Handle:
                    return "Handle:??";
                case ObjectId:
                    return "ObjectId:??";
                case SoftPointerId:
                    return "SoftPointerId:??";
                case HardPointerId:
                    return "HardPointerId:??";
                case SoftOwnershipId:
                    return "SoftOwnershipId:??";
                case HardOwnershipId:
                    return "HardOwnershipId:??";
                case Integer64:
                    return "Integer64:??";

            }
            return "OdResBuf value: " + sysVar.restype() + " not supported";
        }


        void dumpLayers1(OdDbDatabase db, int indent) // C# copy
        {
            /**********************************************************************/
      /* Get a SmartPointer to the LayerTable                               */
            /**********************************************************************/
            OdDbLayerTable pTable = OdDbLayerTable.cast(db.getLayerTableId().safeOpenObject());

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent++, _toString(pTable.isA().name()));

            /**********************************************************************/
      /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator pIter = pTable.newIterator();

            /**********************************************************************/
      /* Step through the LayerTable                                        */
            /**********************************************************************/
            for (pIter.start(); !pIter.done(); pIter.step()) {
                /********************************************************************/
	  	  /* Open the LayerTableRecord for Reading                            */
                /********************************************************************/
                OdDbLayerTableRecord record = OdDbLayerTableRecord.cast(pIter.getRecordId().safeOpenObject());

                /********************************************************************/
      	/* Dump the LayerTableRecord                                        */
                /********************************************************************/
                writeLine();
                writeLine(indent, toString(record.desc()));
                writeLine(indent, "Name", _toString(record.getName()));
                writeLine(indent, "In Use", _toString(record.isInUse()));
                writeLine(indent, "On", _toString(!record.isOff()));
                writeLine(indent, "Frozen", _toString(record.isFrozen()));
                writeLine(indent, "Hidden", _toString(record.isHidden()));
                writeLine(indent, "Erased", _toString(record.isErased()));

                writeLine(indent, "Locked", _toString(record.isLocked()));
                writeLine(indent, "Color", toString(record.color()));
                writeLine(indent, "Linetype", toString(record.linetypeObjectId()));
                writeLine(indent, "Lineweight", toStringLW(record.lineWeight()));
                writeLine(indent, "Plotstyle", _toString(record.plotStyleName()));
                //	record.plotStyleNameId().safeOpenObject();
                writeLine(indent, "Plottable", _toString(record.isPlottable()));
                writeLine(indent, "New VP Freeze", _toString(record.VPDFLT()));
                dumpSymbolTableRecord(record, indent + 1);
            }
        }
        /************************************************************************/
    /* Dump a Symbol Table Record                                           */

        /**
         * ********************************************************************
         */
        void dumpSymbolTableRecord(OdDbSymbolTableRecord pRecord, int indent) {
            writeLine(indent, "Xref dependent = ", _toString(pRecord.isDependent()));
            if (pRecord.isDependent()) {
                writeLine(indent, "Resolved = ", _toString(pRecord.isResolved()));
            }
        }

        /************************************************************************/
	/* Dump the LayerTable                                                  */

        /**
         * ********************************************************************
         */
        void dumpLayers(OdDbDatabase db, int indent) {
            /**********************************************************************/
	  /* Get a SmartPointer to the LayerTable                               */
            /**********************************************************************/
            OdDbLayerTable layerTable = OdDbLayerTable.cast(db.getLayerTableId().safeOpenObject());


            layerTable.generateUsageData();


            /**********************************************************************/
	  /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent++, toString(layerTable.desc()));

            /**********************************************************************/
	  /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator iter = layerTable.newIterator();

            /**********************************************************************/
	  /* Step through the LayerTable                                        */
            /**********************************************************************/
            for (iter.start(); !iter.done(); iter.step()) {
                /********************************************************************/
		/* Open the LayerTableRecord for Reading                            */
                /********************************************************************/
                OdDbLayerTableRecord record = OdDbLayerTableRecord.cast(iter.getRecordId().safeOpenObject());

                /********************************************************************/
		/* Dump the LayerTableRecord                                        */
                /********************************************************************/
                writeLine();
                writeLine(indent, toString(record.desc()));
                writeLine(indent, "Name", _toString(record.getName()));
                writeLine(indent, "In Use", _toString(record.isInUse()));
                writeLine(indent, "On", _toString(!record.isOff()));
                writeLine(indent, "Frozen", _toString(record.isFrozen()));
                writeLine(indent, "Hidden", _toString(record.isHidden()));
                writeLine(indent, "Erased", _toString(record.isErased()));

                writeLine(indent, "Locked", _toString(record.isLocked()));
                writeLine(indent, "Color", toString(record.color()));
                writeLine(indent, "Linetype", toString(record.linetypeObjectId()));
                writeLine(indent, "Lineweight", toStringLW(record.lineWeight()));
                writeLine(indent, "Plotstyle", _toString(record.plotStyleName()));
                //	record.plotStyleNameId().safeOpenObject();
                writeLine(indent, "Plottable", _toString(record.isPlottable()));
                writeLine(indent, "New VP Freeze", _toString(record.VPDFLT()));
            }
        }

        void dumpLinetypes(OdDbDatabase pDb, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the LinetypeTable                            */
            /**********************************************************************/
            // changes related to bug #14152
            OdDbObject pObjId = pDb.getLinetypeTableId().safeOpenObject();
            OdDbLinetypeTable pTable = OdDbLinetypeTable.cast(pObjId);

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent, toString(pTable.isA()));

            /**********************************************************************/
      /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator pIter = pTable.newIterator();

            /**********************************************************************/
      /* Step through the LinetypeTable                                     */
            /**********************************************************************/
            for (pIter.start(); !pIter.done(); pIter.step()) {
                /*********************************************************************/
        /* Open the LinetypeTableRecord for Reading                          */
                /*********************************************************************/
                OdDbLinetypeTableRecord pRecord = OdDbLinetypeTableRecord.cast(pIter.getRecordId().safeOpenObject());

                /********************************************************************/
        /* Dump the LinetypeTableRecord                                      */
                /********************************************************************/
                writeLine();
                writeLine(indent, toString(pRecord.isA()));

                /********************************************************************/
        /* Dump the first line of record as in ACAD.LIN                     */
                /********************************************************************/
                String buffer = "*" + pRecord.getName();
                if (pRecord.comments() != "") {
                    buffer += "," + pRecord.comments();
                }
                writeLine(indent, buffer);

                /********************************************************************/
        /* Dump the second line of record as in ACAD.LIN                    */
                /********************************************************************/
                if (pRecord.numDashes() != 0) {
                    buffer = (pRecord.isScaledToFit() ? "S" : "A");
                    for (int i = 0; i < pRecord.numDashes(); i++) {
                        buffer += "," + _toString(pRecord.dashLengthAt(i));
                        int shapeNumber = pRecord.shapeNumberAt(i);
                        String text = pRecord.textAt(i);

                        /**************************************************************/
            /* Dump the Complex Line                                      */
                        /**************************************************************/
                        if (shapeNumber != 0 || text != "") {
                            OdDbTextStyleTableRecord pTextStyle = OdDbTextStyleTableRecord.cast(pRecord.shapeStyleAt(i).openObject());
                            if (shapeNumber != 0) {
                                buffer += ",[" + _toString(shapeNumber) + ",";
                                if (pTextStyle != null) {
                                    buffer += pTextStyle.fileName();
                                } else {
                                    buffer += "NULL style";
                                }
                            } else {
                                buffer += ",[" + (text) + ",";
                                if (pTextStyle != null) {
                                    //buffer += pTextStyle.getName();
                                    buffer += "some name";
                                } else {
                                    buffer += "NULL style";
                                }
                            }
                            if (pRecord.shapeScaleAt(i) != 0) {
                                buffer += ",S" + _toString(pRecord.shapeScaleAt(i));
                            }
                            if (pRecord.shapeRotationAt(i) != 0) {
                                // in C# Program.toDegreeString instead of toString
                                buffer += ",R" + _toString(pRecord.shapeRotationAt(i));
                            }
                            if (pRecord.shapeOffsetAt(i).getX() != 0) {
                                buffer += ",X" + _toString(pRecord.shapeOffsetAt(i).getX());
                            }
                            if (pRecord.shapeOffsetAt(i).getY() != 0) {
                                buffer += ",Y" + _toString(pRecord.shapeOffsetAt(i).getY());
                            }
                            buffer += "]";
                        }
                    }
                    writeLine(indent, buffer);
                }
                dumpSymbolTableRecord(pRecord, indent + 1);
            }
            // changes related to bug #14152
            pTable.delete();
            pObjId.delete();
        }
        /************************************************************************/
        /* Dump the TextStyleTable                                              */

        /**
         * ********************************************************************
         */
        void dumpTextStyles(OdDbDatabase pDb, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the TextStyleTable                            */
            /**********************************************************************/
            OdDbTextStyleTable pTable = OdDbTextStyleTable.cast(pDb.getTextStyleTableId().safeOpenObject());

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent, toString(pTable.isA()));

            /**********************************************************************/
      /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator pIter = pTable.newIterator();

            /**********************************************************************/
      /* Step through the TextStyleTable                                    */
            /**********************************************************************/
            for (pIter.start(); !pIter.done(); pIter.step()) {
                /*********************************************************************/
        /* Open the TextStyleTableRecord for Reading                         */
                /*********************************************************************/
                OdDbTextStyleTableRecord pRecord = OdDbTextStyleTableRecord.cast(pIter.getRecordId().safeOpenObject());

                /*********************************************************************/
        /* Dump the TextStyleTableRecord                                      */
                /*********************************************************************/
                writeLine();
                writeLine(indent, toString(pRecord.isA()));
                writeLine(indent, "Name = ", _toString(pRecord.getName()));
                writeLine(indent, "Shape File = ", _toString(pRecord.isShapeFile()));
                writeLine(indent, "Text Height = ", _toString(pRecord.textSize()));
                writeLine(indent, "Width Factor = ", _toString(pRecord.xScale()));
                // was Program.toDegreeString
                writeLine(indent, "Obliquing Angle = ", _toString(pRecord.obliquingAngle()));
                writeLine(indent, "Backwards = ", _toString(pRecord.isBackwards()));
                writeLine(indent, "Vertical = ", _toString(pRecord.isVertical()));
                writeLine(indent, "Upside Down = ", _toString(pRecord.isUpsideDown()));
                // was Program.shortenPath
                writeLine(indent, "Filename = ", _toString(pRecord.fileName()));
                // was Program.shortenPath
                writeLine(indent, "BigFont Filename = ", _toString(pRecord.bigFontFileName()));


                // as in underlying C++ references are used we should use arrays
                // the first element if the array will be the value we get
                String[] typeface = {""};
                boolean[] bold = {false};
                boolean[] italic = {false};
                int[] charset = {0};
                int[] pitchAndFamily = {0};
                pRecord.font(typeface, bold, italic, charset, pitchAndFamily);
                writeLine(indent, "Typeface = ", typeface[0]);
                writeLine(indent, "Character Set = ", _toString(charset[0]));
                writeLine(indent, "Bold = ", _toString(bold[0]));
                writeLine(indent, "Italic = ", _toString(italic[0]));
                writeLine(indent, "Font Pitch & Family = ", _toString(pitchAndFamily[0]));
                dumpSymbolTableRecord(pRecord, indent + 1);
            }
        }
        /************************************************************************/
    /* Dump the DimStyleTable                                               */

        /**
         * ********************************************************************
         */
        void dumpDimStyles(OdDbDatabase pDb, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the DimStyleTable                            */
            /**********************************************************************/
            OdDbDimStyleTable pTable = OdDbDimStyleTable.cast(pDb.getDimStyleTableId().safeOpenObject());

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent, toString(pTable.isA()));

            /**********************************************************************/
      /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator pIter = pTable.newIterator();

            /**********************************************************************/
      /* Step through the DimStyleTable                                    */
            /**********************************************************************/
            for (pIter.start(); !pIter.done(); pIter.step()) {
                /*********************************************************************/
        /* Open the DimStyleTableRecord for Reading                         */
                /*********************************************************************/
                OdDbDimStyleTableRecord pRecord = OdDbDimStyleTableRecord.cast(pIter.getRecordId().safeOpenObject());

                /*********************************************************************/
        /* Dump the DimStyleTableRecord                                      */
                /*********************************************************************/
                writeLine();
                writeLine(indent, toString(pTable.isA()));
                writeLine(indent, toString(pRecord.isA()));
                writeLine(indent, "Name = ", _toString(pRecord.getName()));
                // was Program.toArcSymbolTypeString
                writeLine(indent, "Arc Symbol = ", _toString(pRecord.getArcSymbolType()));

                // commented for now as it seems getBgrndTxtColor is not properly wrapped
                //OdCmColor[] bgrndTxtColor = new OdCmColor[1];
                //int bgrndTxtFlags = pRecord.getBgrndTxtColor(bgrndTxtColor[0]);
                //writeLine(indent, "Background Text Color = ", toString(bgrndTxtColor[0]));
                //writeLine(indent, "BackgroundText Flags = ", _toString(bgrndTxtFlags));

                // was Program.toString
                writeLine(indent, "Extension Line 1 Linetype = ", toString(pRecord.getDimExt1Linetype()));
                // was Program.toString
                writeLine(indent, "Extension Line 2 Linetype = ", toString(pRecord.getDimExt2Linetype()));
                // was Program.toString
                writeLine(indent, "Dimension Line Linetype = ", toString(pRecord.getDimExt2Linetype()));
                writeLine(indent, "Extension Line Fixed Len = ", _toString(pRecord.getExtLineFixLen()));
                writeLine(indent, "Extension Line Fixed Len Enable = ", _toString(pRecord.getExtLineFixLenEnable()));
                // was Program.toDegreeString
                writeLine(indent, "Jog Angle = ", _toString(pRecord.getJogAngle()));
                writeLine(indent, "Modified For Recompute = ", _toString(pRecord.isModifiedForRecompute()));
                writeLine(indent, "DIMADEC = ", _toString(pRecord.dimadec()));
                writeLine(indent, "DIMALT = ", _toString(pRecord.dimalt()));
                writeLine(indent, "DIMALTD = ", _toString(pRecord.dimaltd()));
                writeLine(indent, "DIMALTF = ", _toString(pRecord.dimaltf()));
                writeLine(indent, "DIMALTRND = ", _toString(pRecord.dimaltrnd()));
                writeLine(indent, "DIMALTTD = ", _toString(pRecord.dimalttd()));
                writeLine(indent, "DIMALTTZ = ", _toString(pRecord.dimalttz()));
                writeLine(indent, "DIMALTU = ", _toString(pRecord.dimaltu()));
                writeLine(indent, "DIMALTZ = ", _toString(pRecord.dimaltz()));
                writeLine(indent, "DIMAPOST = ", _toString(pRecord.dimapost()));
                writeLine(indent, "DIMASZ = ", _toString(pRecord.dimasz()));
                writeLine(indent, "DIMATFIT = ", _toString(pRecord.dimatfit()));
                writeLine(indent, "DIMAUNIT = ", _toString(pRecord.dimaunit()));
                writeLine(indent, "DIMAZIN = ", _toString(pRecord.dimazin()));
                // was Program.toString
                writeLine(indent, "DIMBLK = ", toString(pRecord.dimblk()));
                // was Program.toString
                writeLine(indent, "DIMBLK1 = ", toString(pRecord.dimblk1()));
                // was Program.toString
                writeLine(indent, "DIMBLK2 = ", toString(pRecord.dimblk2()));
                writeLine(indent, "DIMCEN = ", _toString(pRecord.dimcen()));
                writeLine(indent, "DIMCLRD = ", toString(pRecord.dimclrd()));
                writeLine(indent, "DIMCLRE = ", toString(pRecord.dimclre()));
                writeLine(indent, "DIMCLRT = ", toString(pRecord.dimclrt()));
                writeLine(indent, "DIMDEC = ", _toString(pRecord.dimdec()));
                writeLine(indent, "DIMDLE = ", _toString(pRecord.dimdle()));
                writeLine(indent, "DIMDLI = ", _toString(pRecord.dimdli()));
                writeLine(indent, "DIMDSEP = ", _toString(pRecord.dimdsep()));
                writeLine(indent, "DIMEXE = ", _toString(pRecord.dimexe()));
                writeLine(indent, "DIMEXO = ", _toString(pRecord.dimexo()));
                writeLine(indent, "DIMFRAC = ", _toString(pRecord.dimfrac()));
                writeLine(indent, "DIMGAP = ", _toString(pRecord.dimgap()));
                writeLine(indent, "DIMJUST = ", _toString(pRecord.dimjust()));
                // was Program.toString
                writeLine(indent, "DIMLDRBLK = ", toString(pRecord.dimldrblk()));
                writeLine(indent, "DIMLFAC = ", _toString(pRecord.dimlfac()));
                writeLine(indent, "DIMLIM = ", _toString(pRecord.dimlim()));
                writeLine(indent, "DIMLUNIT = ", _toString(pRecord.dimlunit()));
                writeLine(indent, "DIMLWD = ", toStringLW(pRecord.dimlwd()));
                writeLine(indent, "DIMLWE = ", toStringLW(pRecord.dimlwe()));
                writeLine(indent, "DIMPOST = ", _toString(pRecord.dimpost()));
                writeLine(indent, "DIMRND = ", _toString(pRecord.dimrnd()));
                writeLine(indent, "DIMSAH = ", _toString(pRecord.dimsah()));
                writeLine(indent, "DIMSCALE = ", _toString(pRecord.dimscale()));
                writeLine(indent, "DIMSD1 = ", _toString(pRecord.dimsd1()));
                writeLine(indent, "DIMSD2 = ", _toString(pRecord.dimsd2()));
                writeLine(indent, "DIMSE1 = ", _toString(pRecord.dimse1()));
                writeLine(indent, "DIMSE2 = ", _toString(pRecord.dimse2()));
                writeLine(indent, "DIMSOXD = ", _toString(pRecord.dimsoxd()));
                writeLine(indent, "DIMTAD = ", _toString(pRecord.dimtad()));
                writeLine(indent, "DIMTDEC = ", _toString(pRecord.dimtdec()));
                writeLine(indent, "DIMTFAC = ", _toString(pRecord.dimtfac()));
                writeLine(indent, "DIMTIH = ", _toString(pRecord.dimtih()));
                writeLine(indent, "DIMTIX = ", _toString(pRecord.dimtix()));
                writeLine(indent, "DIMTM = ", _toString(pRecord.dimtm()));
                writeLine(indent, "DIMTOFL = ", _toString(pRecord.dimtofl()));
                writeLine(indent, "DIMTOH = ", _toString(pRecord.dimtoh()));
                writeLine(indent, "DIMTOL = ", _toString(pRecord.dimtol()));
                writeLine(indent, "DIMTOLJ = ", _toString(pRecord.dimtolj()));
                writeLine(indent, "DIMTP = ", _toString(pRecord.dimtp()));
                writeLine(indent, "DIMTSZ = ", _toString(pRecord.dimtsz()));
                writeLine(indent, "DIMTVP = ", _toString(pRecord.dimtvp()));
                // was Program.toString
                writeLine(indent, "DIMTXSTY = ", toString(pRecord.dimtxsty()));
                writeLine(indent, "DIMTXT = ", _toString(pRecord.dimtxt()));
                writeLine(indent, "DIMTZIN = ", _toString(pRecord.dimtzin()));
                writeLine(indent, "DIMUPT = ", _toString(pRecord.dimupt()));
                writeLine(indent, "DIMZIN = ", _toString(pRecord.dimzin()));
                dumpSymbolTableRecord(pRecord, indent + 1);
            }
        }
/////////////////////////////////////////////////////////////
////////// copied from C# start /////////////////////////////
/////////////////////////////////////////////////////////////    	

        /************************************************************************/
    /* Dump the RegAppTable                                              */

        /**
         * ********************************************************************
         */
        void dumpRegApps(OdDbDatabase pDb, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the RegAppTable                            */
            /**********************************************************************/
            //OdDbRegAppTable pTable = OdDbRegAppTable.get(pDb.getRegAppTableId().safeOpenObject());
            OdDbRegAppTable pTable = OdDbRegAppTable.cast(pDb.getRegAppTableId().safeOpenObject());

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent, toString(pTable.isA()));

            /**********************************************************************/
      /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator pIter = pTable.newIterator();

            /**********************************************************************/
      /* Step through the RegAppTable                                    */
            /**********************************************************************/
            for (pIter.start(); !pIter.done(); pIter.step()) {
                /*********************************************************************/
        /* Open the RegAppTableRecord for Reading                         */
                /*********************************************************************/
                OdDbRegAppTableRecord pRecord = OdDbRegAppTableRecord.cast(pIter.getRecordId().safeOpenObject());

                /*********************************************************************/
        /* Dump the RegAppTableRecord                                      */
                /*********************************************************************/
                writeLine();
                writeLine(indent, toString(pRecord.isA()));
                writeLine(indent, "Name = ", pRecord.getName());
            }
        }

        /************************************************************************/
    /* Dump the AbstractViewTableRecord                                     */

        /**
         * *********************************************************************
         */
        void dumpAbstractViewTableRecord(OdDbAbstractViewTableRecord pView, int indent) {

            /*********************************************************************/
      /* Dump the AbstractViewTableRecord                                  */
            /*********************************************************************/
            writeLine(indent, "Back Clip Dist = ", _toString(pView.backClipDistance()));
            writeLine(indent, "Back Clip Enabled = ", _toString(pView.backClipEnabled()));
            writeLine(indent, "Front Clip Dist = ", _toString(pView.frontClipDistance()));
            writeLine(indent, "Front Clip Enabled = ", _toString(pView.frontClipEnabled()));
            writeLine(indent, "Front Clip at Eye = ", _toString(pView.frontClipAtEye()));
            writeLine(indent, "Elevation = ", _toString(pView.elevation()));
            writeLine(indent, "Height = ", _toString(pView.height()));
            writeLine(indent, "Width = ", _toString(pView.width()));
            writeLine(indent, "Lens Length = ", _toString(pView.lensLength()));
            //writeLine(indent, "Render Mode = ", _toString(pView.renderMode())); // no _toString method found for it
            writeLine(indent, "Perspective = ", _toString(pView.perspectiveEnabled()));
            // was Program.toString
            //writeLine(indent, "UCS Name = ", _toString(pView.ucsName())); // no _toString found

            // method isUcsOrthographic is wrapped incorrectly
            //OrthographicView orthoUCS = new OrthographicView();
            //writeLine(indent, "UCS Orthographic = ", _toString(pView.isUcsOrthographic(orthoUCS[0])));
            //writeLine(indent, "Orthographic UCS = ", _toString(orthoUCS[0]));

            // getUcs seems to be wrapped incorrectly
            OdGePoint3d origin = new OdGePoint3d();
            OdGeVector3d xAxis = new OdGeVector3d();
            OdGeVector3d yAxis = new OdGeVector3d();
            //pView.getUcs(origin, xAxis, yAxis);
            //writeLine(indent, "UCS Origin = ", _toString(origin));
            //writeLine(indent, "UCS x-Axis = ", _toString(xAxis));
            //writeLine(indent, "UCS y-Axis = ", _toString(yAxis));
            writeLine(indent, "Target = ", _toString(pView.target()));
            writeLine(indent, "View Direction = ", _toString(pView.viewDirection()));
            // was Program.toDegreeString
            writeLine(indent, "Twist Angle = ", _toString(pView.viewTwist()));
            dumpSymbolTableRecord(pView, indent + 1);
        }

        /************************************************************************/
    /* Dump the ViewportTable                                              */

        /**
         * ********************************************************************
         */
        void dumpViewports(OdDbDatabase pDb, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the ViewportTable                            */
            /**********************************************************************/
            OdDbViewportTable pTable = OdDbViewportTable.cast(pDb.getViewportTableId().safeOpenObject());

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent, toString(pTable.isA()));

            /**********************************************************************/
      /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator pIter = pTable.newIterator();

            /**********************************************************************/
      /* Step through the ViewportTable                                    */
            /**********************************************************************/
            for (pIter.start(); !pIter.done(); pIter.step()) {
                /*********************************************************************/
        /* Open the ViewportTableRecord for Reading                          */
                /*********************************************************************/
                OdDbViewportTableRecord pRecord = OdDbViewportTableRecord.cast(pIter.getRecordId().safeOpenObject());

                /*********************************************************************/
        /* Dump the ViewportTableRecord                                      */
                /*********************************************************************/
                writeLine();
                writeLine(indent, toString(pRecord.isA()));
                writeLine(indent, "Name = ", pRecord.getName());
                writeLine(indent, "Circle Sides = ", _toString(pRecord.circleSides()));
                writeLine(indent, "Fast Zooms Enabled = ", _toString(pRecord.fastZoomsEnabled()));
                writeLine(indent, "Grid Enabled = ", _toString(pRecord.gridEnabled()));
                writeLine(indent, "Grid Increments = ", _toString(pRecord.gridIncrements()));
                writeLine(indent, "Icon at Origin = ", _toString(pRecord.iconAtOrigin()));
                writeLine(indent, "Icon Enabled = ", _toString(pRecord.iconEnabled()));
                writeLine(indent, "Iso snap Enabled = ", _toString(pRecord.isometricSnapEnabled()));
                writeLine(indent, "Iso Snap Pair = ", _toString(pRecord.snapPair()));
                writeLine(indent, "UCS Saved w/Vport = ", _toString(pRecord.isUcsSavedWithViewport()));
                writeLine(indent, "UCS follow = ", _toString(pRecord.ucsFollowMode()));
                writeLine(indent, "Lower-Left Corner = ", _toString(pRecord.lowerLeftCorner()));
                writeLine(indent, "Upper-Right Corner = ", _toString(pRecord.upperRightCorner()));
                // was Program.toDegreeString
                writeLine(indent, "Snap Angle = ", _toString(pRecord.snapAngle()));
                writeLine(indent, "Snap Base = ", _toString(pRecord.snapBase()));
                writeLine(indent, "Snap Enabled = ", _toString(pRecord.snapEnabled()));
                writeLine(indent, "Snap Increments = ", _toString(pRecord.snapIncrements()));
                dumpAbstractViewTableRecord(pRecord, indent + 1);
            }
        }

        /************************************************************************/
    /* Dump the ViewTable                                                   */

        /**
         * ********************************************************************
         */
        void dumpViews1(OdDbDatabase pDb, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the ViewTable                                */
            /**********************************************************************/
            OdDbViewTable pTable = OdDbViewTable.cast(pDb.getViewTableId().safeOpenObject());

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent, toString(pTable.isA()));

            /**********************************************************************/
      /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator pIter = pTable.newIterator();

            /**********************************************************************/
      /* Step through the ViewTable                                         */
            /**********************************************************************/
            for (pIter.start(); !pIter.done(); pIter.step()) {
                /*********************************************************************/
        /* Open the ViewTableRecord for Reading                              */
                /*********************************************************************/
                OdDbViewTableRecord pRecord = OdDbViewTableRecord.cast(pIter.getRecordId().safeOpenObject());

                /*********************************************************************/
        /* Dump the ViewTableRecord                                          */
                /*********************************************************************/
                writeLine();
                writeLine(indent, toString(pRecord.isA()));
                writeLine(indent, "Name = ", pRecord.getName());
                writeLine(indent, "Category Name = ", pRecord.getCategoryName());
                writeLine(indent, "Layer State = ", _toString(pRecord.getLayerState()));

                String layoutName = "";
                if (!pRecord.getLayout().isNull()) {
                    OdDbLayout pLayout = OdDbLayout.cast(pRecord.getLayout().safeOpenObject());
                    layoutName = pLayout.getLayoutName();
                }
                writeLine(indent, "Layout Name = ", (layoutName));
                writeLine(indent, "PaperSpace View = ", _toString(pRecord.isPaperspaceView()));
                writeLine(indent, "Associated UCS = ", _toString(pRecord.isUcsAssociatedToView()));
                writeLine(indent, "PaperSpace View = ", _toString(pRecord.isViewAssociatedToViewport()));
                dumpAbstractViewTableRecord(pRecord, indent + 1);
            }
        }
        /************************************************************************/
    /* Dump the MlineStyle Dictionary                                       */

        /**
         * ********************************************************************
         */
        void dumpMLineStyles(OdDbDatabase pDb, int indent) {
            OdDbDictionary pDictionary = OdDbDictionary.cast(pDb.getMLStyleDictionaryId().safeOpenObject());

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent, toString(pDictionary.isA()));

            /**********************************************************************/
      /* Get a SmartPointer to a new DictionaryIterator                     */
            /**********************************************************************/
            OdDbDictionaryIterator pIter = pDictionary.newIterator();

            /**********************************************************************/
      /* Step through the MlineStyle dictionary                             */
            /**********************************************************************/
            for (; !pIter.done(); pIter.next()) {
                OdDbObjectId id = pIter.objectId();
                OdDbMlineStyle pEntry = OdDbMlineStyle.cast(id.safeOpenObject());
                if (pEntry == null)
                    continue;

                /*********************************************************************/
        /* Dump the MLineStyle dictionary entry                              */
                /*********************************************************************/
                writeLine();
                writeLine(indent, toString(pEntry.isA()));
                writeLine(indent, "Name = ", pEntry.name());
                writeLine(indent, "Description = ", pEntry.description());
                // was Program.toDegreeString
                writeLine(indent, "Start Angle = ", _toString(pEntry.startAngle()));
                // was Program.toDegreeString
                writeLine(indent, "End Angle = ", _toString(pEntry.endAngle()));
                writeLine(indent, "Start Inner Arcs = ", _toString(pEntry.startInnerArcs()));
                writeLine(indent, "End Inner Arcs = ", _toString(pEntry.endInnerArcs()));
                writeLine(indent, "Start Round Cap = ", _toString(pEntry.startRoundCap()));
                writeLine(indent, "End Round Cap = ", _toString(pEntry.endRoundCap()));
                writeLine(indent, "Start Square Cap = ", _toString(pEntry.startRoundCap()));
                writeLine(indent, "End Square Cap = ", _toString(pEntry.endRoundCap()));
                writeLine(indent, "Show Miters = ", _toString(pEntry.showMiters()));
                /*********************************************************************/
        /* Dump the elements                                                 */
                /*********************************************************************/
                if (pEntry.numElements() != 0) {
                    writeLine(indent, "Elements:");
                }
                for (int i = 0; i < pEntry.numElements(); i++) {
                    double[] offset = new double[1];
                    OdCmColor color = new OdCmColor();
                    OdDbObjectId linetypeId = new OdDbObjectId();
                    // method getElementAt is wrapped incorrectly          
                    //pEntry.getElementAt(i, offset, color, linetypeId);
                    //writeLine(indent+1, "Index = ", toString(i));
                    //writeLine(indent+1, "Offset = ", toString(offset[0]));
                    //writeLine(indent+1, "Color = ", toString(color));
                    // was Program.toString
                    writeLine(indent + 1, "Linetype = ", toString(linetypeId));
                }
            }
        }
        /************************************************************************/
    /* Dump the UCSTable                                                    */

        /**
         * ********************************************************************
         */
        void dumpUCSTable(OdDbDatabase pDb, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the UCSTable                               */
            /**********************************************************************/
            OdDbUCSTable pTable = OdDbUCSTable.cast(pDb.getUCSTableId().safeOpenObject());

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent, toString(pTable.isA()));

            /**********************************************************************/
      /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator pIter = pTable.newIterator();

            /**********************************************************************/
      /* Step through the UCSTable                                          */
            /**********************************************************************/
            for (pIter.start(); !pIter.done(); pIter.step()) {
                /********************************************************************/
        /* Open the UCSTableRecord for Reading                            */
                /********************************************************************/
                OdDbUCSTableRecord pRecord = OdDbUCSTableRecord.cast(pIter.getRecordId().safeOpenObject());

                /********************************************************************/
        /* Dump the UCSTableRecord                                        */
                /********************************************************************/
                writeLine();
                writeLine(indent, toString(pRecord.isA()));
                writeLine(indent, "Name = ", pRecord.getName());
                writeLine(indent, "UCS Origin = ", _toString(pRecord.origin()));
                writeLine(indent, "UCS x-Axis = ", _toString(pRecord.xAxis()));
                writeLine(indent, "UCS y-Axis = ", _toString(pRecord.yAxis()));
                dumpSymbolTableRecord(pRecord, indent + 1);
            }
        }

        /************************************************************************/
    /* Dump the Entity                                                      */

        /**
         * ********************************************************************
         */
        void dumpEntity(OdDbObjectId id, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the Entity                                   */
            /**********************************************************************/

            // changes for bug #14152 first part
            // we explicitly create OdDbObject and entity object

            //instead of OdDbEntity pEnt = OdDbEntity.cast(id.openObject());
            OdDbObject pId = id.openObject();
            OdDbEntity pEnt = OdDbEntity.cast(pId);

            if (pEnt == null)
                return;

            /**********************************************************************/
      /* Dump the entity                                                    */
            /**********************************************************************/
            writeLine();
            pEnt.list();

            /**********************************************************************/
      /* Dump the Xdata                                                     */
            /**********************************************************************/
            dumpXdata(pEnt.xData(), indent);

            /**********************************************************************/
      /* Dump the Extension Dictionary                                      */
            /**********************************************************************/
            if (!pEnt.extensionDictionary().isNull()) {
                dumpObject(pEnt.extensionDictionary(), "ACAD_XDICTIONARY", indent);
            }
            // changes for bug #14152 second part
            // we explicitly delete entity object and OdDbObject
            pEnt.delete();
            pId.delete();
        }

        /************************************************************************/
    /* Dump the BlockTable                                                  */

        /**
         * ********************************************************************
         */
        void dumpBlocks(OdDbDatabase pDb, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the BlockTable                               */
            /**********************************************************************/
            OdDbBlockTable pTable = OdDbBlockTable.cast(pDb.getBlockTableId().safeOpenObject());

            /**********************************************************************/
      /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent, toString(pTable.isA()));

            /**********************************************************************/
      /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator pBlkIter = pTable.newIterator();

            /**********************************************************************/
      /* Step through the BlockTable                                        */
            /**********************************************************************/
            for (pBlkIter.start(); !pBlkIter.done(); pBlkIter.step()) {
                /********************************************************************/
        /* Open the BlockTableRecord for Reading                            */
                /********************************************************************/
                OdDbBlockTableRecord pBlock = OdDbBlockTableRecord.cast(pBlkIter.getRecordId().safeOpenObject());

                /********************************************************************/
        /* Dump the BlockTableRecord                                        */
                /********************************************************************/
                writeLine();
                writeLine(indent, toString(pBlock.isA()));
                writeLine(indent, "Name = ", pBlock.getName());
                writeLine(indent, "Anonymous = ", _toString(pBlock.isAnonymous()));
                writeLine(indent, "Comments = ", pBlock.comments());
                writeLine(indent, "Origin = ", _toString(pBlock.origin()));
                // no proper toString method
                //writeLine(indent, "Block Insert Units = ", toString(pBlock.blockInsertUnits()));
                //writeLine(indent, "Block Scaling = ", toString(pBlock.blockScaling()));
                writeLine(indent, "Explodable = ", _toString(pBlock.explodable()));

                OdGeExtents3d extents = new OdGeExtents3d();
                //extents[1] = new OdGeExtents3d();
                // getGeomExtents seems to be wrapped incorrectly, check whether array should be
                if (OdResult.eOk == pBlock.getGeomExtents(extents)) {
                    writeLine(indent, "Min Extents = ", _toString(extents.minPoint()));
                    writeLine(indent, "Max Extents = ", _toString(extents.maxPoint()));
                }
                writeLine(indent, "Layout = ", _toString(pBlock.isLayout()));
                writeLine(indent, "Has Attribute Definitions = ", _toString(pBlock.hasAttributeDefinitions()));
                // no proper toString method
                //writeLine(indent, "Xref Status = ", toString(pBlock.xrefStatus()));
                if (pBlock.xrefStatus() != XrefStatus.kXrfNotAnXref) {
                    writeLine(indent, "Xref Path = ", pBlock.pathName());
                    writeLine(indent, "From Xref Attach = ", _toString(pBlock.isFromExternalReference()));
                    writeLine(indent, "From Xref Overlay = ", _toString(pBlock.isFromOverlayReference()));
                    writeLine(indent, "Xref Unloaded = ", _toString(pBlock.isUnloaded()));
                }
                /********************************************************************/
        /* Get a SmartPointer to a new ObjectIterator                       */
                /********************************************************************/
                OdDbObjectIterator pEntIter = pBlock.newIterator();

                /********************************************************************/
        /* Step through the BlockTableRecord                                */
                /********************************************************************/
                for (; !pEntIter.done(); pEntIter.step()) {
                    /********************************************************************/
          /* Dump the Entity                                                  */
                    /********************************************************************/
                    dumpEntity(pEntIter.objectId(), indent + 1);
                }
            }
        }

        /************************************************************************/
    /* Dump Xdata                                                           */

        /**
         * ********************************************************************
         */
        void dumpXdata(OdResBuf resbuf, int indent) {
            if (resbuf == null)
                return;

            writeLine(indent, "Xdata:");

            /**********************************************************************/
      /* Step through the ResBuf chain                                      */
            /**********************************************************************/
            for (; resbuf != null; resbuf = resbuf.next()) {
                //writeLine(indent, "Here's the failure");
                int code = resbuf.restype();

                /********************************************************************/
        /* Convert resbuf.ResVal to a string                               */
                /********************************************************************/
                String rightString = "???";
                switch (OdDxfCode._getType(code)) {
                    case Name://case OdDxfCode.Type.Name:
                    case String://case OdDxfCode.Type.String:
                    case LayerName://case OdDxfCode.Type.LayerName:
                        rightString = (resbuf.getString());
                        break;

                    case Bool://case OdDxfCode.Type.Bool:
                        rightString = _toString(resbuf.getBool());
                        break;

                    case Integer8://case OdDxfCode.Type.Integer8:
                        rightString = _toString(resbuf.getInt8());
                        break;

                    case Integer16://case OdDxfCode.Type.Integer16:
                        rightString = _toString(resbuf.getInt16());
                        break;

                    case Integer32://case OdDxfCode.Type.Integer32:
                        rightString = _toString(resbuf.getInt32());
                        break;

                    case Double://case OdDxfCode.Type.Double:
                        rightString = _toString(resbuf.getDouble());
                        break;

                    case Angle://case OdDxfCode.Type.Angle:
                        // was Program.todegreeString
                        rightString = _toString(resbuf.getDouble());
                        break;

                    case Point://case OdDxfCode.Type.Point:
                    {
                        rightString = _toString(resbuf.getPoint3d());
                    }
                    break;

                    case BinaryChunk://case OdDxfCode.Type.BinaryChunk:
                        rightString = "<Binary Data>";
                        break;

                    case ObjectId://case OdDxfCode.Type.ObjectId:
                    case SoftPointerId://case OdDxfCode.Type.SoftPointerId:
                    case HardPointerId://case OdDxfCode.Type.HardPointerId:
                    case SoftOwnershipId://case OdDxfCode.Type.SoftOwnershipId:
                    case HardOwnershipId://case OdDxfCode.Type.HardOwnershipId:
                    case Handle://case OdDxfCode.Type.Handle:
                        // no proper toString method
                        //rightString = _tostring(resbuf.getHandle());
                        break;

                    case Unknown://case OdDxfCode.Type.Unknown:
                    default:
                        rightString = "Unknown";
                        break;
                }
                writeLine(indent, _toString(code) + " " + rightString);
            }
        }

        /************************************************************************/
    /* Dump the Xref the full path to the Osnap entity                      */

        /**
         * ********************************************************************
         */
        void dumpXrefFullSubentPath(OdDbXrefFullSubentPath subEntPath, int indent) {
            // was (int)subEntPath.subentId().index()
            // returns SWIGTYPE_p_OdGsMarker, so method seems to wrapped incorrectly
            //writeLine(indent, "Subentity Index = ", _toString(subEntPath.subentId().index()));
            // no proper toString
            //writeLine(indent, "Subentity Type = ", _toString(subEntPath.subentId().type()));
            for (int j = 0; j < subEntPath.objectIds().size(); j++) {
                // can't call openObject
                //OdDbEntity pEnt = OdDbEntity.cast(subEntPath.objectIds().get(j).openObject());
                //if (pEnt != null)
                //{
                //  writeLine(indent, "<" + toString(pEnt.isA()) + "> ", toString(pEnt.getDbHandle()));
                //}
            }
        }

        /************************************************************************/
    /* Dump Object Snap Point Reference for an Associative Dimension        */

        /**
         * ********************************************************************
         */
        void dumpOsnapPointRef(OdDbOsnapPointRef pRef, int index, int indent) {
            writeLine(indent, "<" + toString(pRef.isA()) + ">", _toString(index));
            // no proper _toString method
            //writeLine(indent, "Osnap Mode = ", _toString(pRef.osnapType()));
            writeLine(indent, "Near Osnap = ", _toString(pRef.nearPointParam()));
            writeLine(indent, "Osnap Point = ", _toString(pRef.point()));

            writeLine(indent, "Main Entity");
            dumpXrefFullSubentPath(pRef.mainEntity(), indent + 1);

            writeLine(indent, "Intersect Entity");
            dumpXrefFullSubentPath(pRef.intersectEntity(), indent + 1);

            if (pRef.lastPointRef() != null) {
                writeLine(indent, "Last Point Referenced");
                dumpOsnapPointRef(pRef.lastPointRef(), index + 1, indent);
            } else {
                writeLine(indent, "Last Point Referenced = Null");
            }
        }

        /************************************************************************/
    /* Dump an Associative dimension                                        */

        /**
         * ********************************************************************
         */
        void dumpDimAssoc(OdDbObject pObject, int indent) {
            OdDbDimAssoc pDimAssoc = OdDbDimAssoc.cast(pObject);
            // no proper toString method
            //writeLine(indent, "Associative = ",
            //     _toString((OdDbDimAssoc.AssocFlags)pDimAssoc.assocFlag()));
            writeLine(indent, "TransSpatial = ", _toString(pDimAssoc.isTransSpatial()));
            // no proper _toString method
            //writeLine(indent, "Rotated Type = ", _toString(pDimAssoc.rotatedDimType()));

            for (int i = 0; i < OdDbDimAssoc.kMaxPointRefs; i++) {
                OdDbOsnapPointRef pRef = pDimAssoc.pointRef(i);
                if (pRef != null) {
                    dumpOsnapPointRef(pRef, i, indent + 1);
                } else {
                    break;
                }
            }
        }

        /************************************************************************/
    /* Dump the object.                                                      */
    /*                                                                      */
    /* Dictionary objects are recursively dumped.                           */
    /* XRecord objects are dumped.                                          */
    /* DimAssoc objects are dumped.                                         */

        /**
         * ********************************************************************
         */
        void dumpObject(OdDbObjectId id, String itemName, int indent) {
            /**********************************************************************/
      /* Get a SmartPointer to the object                                   */
            /**********************************************************************/
            OdDbObject pObject = id.safeOpenObject();

            /**********************************************************************/
      /* Dump the item name and class name                                  */
            /**********************************************************************/
            if (pObject.isKindOf(OdDbDictionary.desc())) {
                writeLine();
            }
            writeLine(indent, itemName, toString(pObject.isA()));

            /**********************************************************************/
      /* Dispatch                                                           */
            /**********************************************************************/
            if (pObject.isKindOf(OdDbDictionary.desc())) {
                /********************************************************************/
        /* Dump the dictionary                                               */
                /********************************************************************/
                OdDbDictionary pDic = OdDbDictionary.cast(pObject);

                /********************************************************************/
        /* Get a SmartPointer to a new DictionaryIterator                   */
                /********************************************************************/
                OdDbDictionaryIterator pIter = pDic.newIterator();

                /********************************************************************/
        /* Step through the Dictionary                                      */
                /********************************************************************/
                for (; !pIter.done(); pIter.next()) {
                    /******************************************************************/
          /* Dump the Dictionary object                                     */
                    /******************************************************************/
                    // Dump each item in the dictionary.
                    dumpObject(pIter.objectId(), pIter.name(), indent + 1);
                }
            } else if (pObject.isKindOf(OdDbXrecord.desc())) {
                /********************************************************************/
        /* Dump an Xrecord                                                  */
                /********************************************************************/
                OdDbXrecord pXRec = OdDbXrecord.cast(pObject);
                dumpXdata(pXRec.rbChain(), indent + 1);
            } else if (pObject.isKindOf(OdDbDimAssoc.desc())) {
                /********************************************************************/
        /* Dump an Associative dimension                                    */
                /********************************************************************/
                dumpDimAssoc(pObject, indent + 1);
            } else if (pObject.isKindOf(OdDbProxyObject.desc())) {
                OdDbProxyExt proxyEntExt = OdDbProxyExt.cast(pObject);

                writeLine(indent, "Proxy OriginalClassName = ", (proxyEntExt.originalClassName(pObject)));
                writeLine(indent, "Proxy ApplicationDescription = ", (proxyEntExt.applicationDescription(pObject)));
                writeLine(indent, "Proxy OriginalDxfName = ", (proxyEntExt.originalDxfName(pObject)));
            }
        }

/////////////////////////////////////////////////////////////
////////// copied from C# finish ////////////////////////////
/////////////////////////////////////////////////////////////
        /************************************************************************/
	/* Dump the LayerTable                                                  */

        /**
         * ********************************************************************
         */
        void dumpDictionaries(OdDbDatabase db, int indent) {
            dumpDictionary("PlotSettings", db.getPlotSettingsDictionaryId(), indent);
            dumpDictionary("LayoutDictionary", db.getLayoutDictionaryId(), indent);
            dumpDictionary("getMaterialDictionaryId", db.getMaterialDictionaryId(), indent);
            dumpDictionary("getMLStyleDictionaryId", db.getMLStyleDictionaryId(), indent);
            dumpDictionary("getMLeaderStyleDictionaryId", db.getMLeaderStyleDictionaryId(), indent);
            dumpDictionary("getTableStyleDictionaryId", db.getTableStyleDictionaryId(), indent);
            dumpDictionary("getVisualStyleDictionaryId", db.getVisualStyleDictionaryId(), indent);
            dumpDictionary("getPlotStyleNameDictionaryId", db.getPlotStyleNameDictionaryId(), indent);
            dumpDictionary("getGroupDictionaryId", db.getGroupDictionaryId(), indent);
            dumpDictionary("getColorDictionaryId", db.getColorDictionaryId(), indent);
            dumpDictionary("getScaleListDictionaryId", db.getScaleListDictionaryId(), indent);
        }

        void dumpDictionary(String label, OdDbObjectId obj, int indent) {
            System.out.println("=== Dictionay : " + label + " ===");
            OdDbDictionary dictionary = OdDbDictionary.cast(obj.safeOpenObject());

            for (OdDbDictionaryIterator itDict = dictionary.newIterator(); !itDict.done(); itDict.next()) {
                System.out.format("\n%s-\"%s\"-%s", itDict.objectId().getHandle().ascii(), itDict.name(), itDict.getObject().isA().name());
            }
            System.out.println("\n\n");
        }

        /************************************************************************/
	/* Dump the ViewTable                                                   */

        /**
         * ********************************************************************
         */
        void dumpViews(OdDbDatabase db, int indent) {
            /**********************************************************************/
	  /* Get a SmartPointer to the ViewTable                                */
            /**********************************************************************/
            OdDbViewTable table = OdDbViewTable.cast(db.getViewTableId().safeOpenObject());

            /**********************************************************************/
	  /* Dump the Description                                               */
            /**********************************************************************/
            writeLine();
            writeLine(indent++, toString(table.desc()));

            /**********************************************************************/
	  /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator iter = ((OdDbSymbolTable) table).newIterator();

            /**********************************************************************/
	  /* Step through the ViewTable                                         */
            /**********************************************************************/
            for (iter.start(); !iter.done(); iter.step()) {
                /*********************************************************************/
	    /* Open the ViewTableRecord for Reading                              */
                /*********************************************************************/
                OdDbViewTableRecord record = OdDbViewTableRecord.cast(iter.getRecordId().safeOpenObject());

                /*********************************************************************/
	    /* Dump the ViewTableRecord                                          */
                /*********************************************************************/
                writeLine();
                writeLine(indent, toString(record.desc()));
                //  writeLine(indent, "Name",              toString(record.getName()));
                writeLine(indent, "Category Name", _toString(record.getCategoryName()));
                writeLine(indent, "Layer State", _toString(record.getLayerState()));

                String layoutName = "";
                if (!record.getLayout().isNull()) {
                    OdDbLayout layout = OdDbLayout.cast(record.getLayout().safeOpenObject());
                    layoutName = layout.getLayoutName();
                }
                writeLine(indent, "Layout Name", _toString(layoutName));
                writeLine(indent, "PaperSpace View", _toString(record.isPaperspaceView()));
                writeLine(indent, "Associated UCS", _toString(record.isUcsAssociatedToView()));
                writeLine(indent, "PaperSpace View", _toString(record.isViewAssociatedToViewport()));
                //dumpAbstractViewTableRecord(record, indent);
            }
        }

//	void dumpAbstractViewTableRecord(OdDbAbstractViewTableRecord view, int indent) {
//		  
//	    /*********************************************************************/
//	    /* Dump the AbstractViewTableRecord                                  */
//	    /*********************************************************************/
//	    writeLine(indent, "Back Clip Dist",      _toString(view.backClipDistance()));
//	    writeLine(indent, "Back Clip Enabled",   _toString(view.backClipEnabled()));
//	    writeLine(indent, "Front Clip Dist",     _toString(view.frontClipDistance()));
//	    writeLine(indent, "Front Clip Enabled",  _toString(view.frontClipEnabled()));
//	    writeLine(indent, "Front Clip at Eye",   _toString(view.frontClipAtEye()));
//	    writeLine(indent, "Elevation",           _toString(view.elevation()));
//	    writeLine(indent, "Height",              _toString(view.height()));
//	    writeLine(indent, "Width",               _toString(view.width()));
//	    writeLine(indent, "Lens Length",         _toString(view.lensLength()));
//	    writeLine(indent, "Render Mode",         _toString(view.renderMode()));
//	    writeLine(indent, "Perspective",         _toString(view.perspectiveEnabled()));
//	    writeLine(indent, "UCS Name",            toString(view.ucsName()));  
//
//	    OrthographicView orthoUCS;       
//	    writeLine(indent, "UCS Orthographic",    toString(view.isUcsOrthographic(orthoUCS)));
//	    writeLine(indent, "Orthographic UCS",    toString(orthoUCS));
//	    OdGePoint3d origin;
//	    OdGeVector3d xAxis;
//	    OdGeVector3d yAxis;
//	    view.getUcs(origin, xAxis, yAxis);
//	    writeLine(indent, "UCS Origin",          _toString(origin));        
//	    writeLine(indent, "UCS x-Axis",          _toString(xAxis));      
//	    writeLine(indent, "UCS y-Axis",          _toString(yAxis));
//	    writeLine(indent, "Target",              _toString(view.target()));       
//	    writeLine(indent, "View Direction",      _toString(view.viewDirection()));       
//	    writeLine(indent, "Twist Angle",         toDegreeString(view.viewTwist()));
//	    dumpSymbolTableRecord(pView, indent);
//	}


        /************************************************************************/
	/* Dump the BlockTable                                                  */

        /**
         * ********************************************************************
         */
        void dumpSupportedBlocks(OdDbDatabase db, int indent) {
            /**********************************************************************/
	  /* Get a SmartPointer to the BlockTable                               */
            /**********************************************************************/
            // changes for bug #14152 first part
            // explicitly create OdDbObject and blockTable object
            OdDbObject pId = db.getBlockTableId().safeOpenObject();
            OdDbBlockTable blockTable = OdDbBlockTable.cast(pId);

            /**********************************************************************/
	  /* Get a SmartPointer to a new SymbolTableIterator                    */
            /**********************************************************************/
            OdDbSymbolTableIterator blockIter = OdDbSymbolTableIterator.cast(blockTable.newIterator());

            /**********************************************************************/
	  /* Step through the BlockTable                                        */
            /**********************************************************************/
            for (blockIter.start(); !blockIter.done(); blockIter.step()) {
                OdDbBlockTableRecord block = OdDbBlockTableRecord.cast(blockIter.getRecordId().safeOpenObject());

                if (block.isLayout()) {
                    OdDbLayout layout = OdDbLayout.cast(block.getLayoutId().safeOpenObject());
                    dumpLayout(layout, indent);
                }

                OdDbObjectIterator entityIter = OdDbObjectIterator.cast(block.newIterator());

                /********************************************************************/
		/* Step through the BlockTableRecord                                */
                /********************************************************************/
                for (; !entityIter.done(); entityIter.step()) {
                    OdDbObject obj = entityIter.objectId().openObject();
                    OdDbEntity objEntity = OdDbEntity.cast(obj);
                    if (!_toString(objEntity.layer()).equalsIgnoreCase("PIPELINE-KAQ-0803-TEXT")) {
                        continue;
                    }
                    writeLine(indent, "openObject() case1");
//                    OdDbObject obj = entityIter.objectId().openObject();
                    writeLine();
                    dumpDbInfo(obj, indent);
                    writeLine(indent, "inside for");

                    int childIndent = indent + 2;
                    if (obj.isKindOf(OdDbLine.desc())) { // obj instanceof OdDbLine
                        OdDbLine line = OdDbLine.cast(obj);
                        dumpSimpleLineAttributes(line, childIndent);
                        dumpCommonEntityData(line, childIndent);
                    } else if (obj.isKindOf(OdDbPolyline.desc())) { // obj instanceof OdDbPolyline
                        OdDbPolyline poly = OdDbPolyline.cast(obj);
                        dumpSimplePolylineAttributes(poly, childIndent);
                        dumpPolylineVertices(poly, childIndent);
                        dumpCommonEntityData(poly, childIndent);
                    } else if (obj.isKindOf(OdDbText.desc())) { // obj instanceof OdDbText
                        OdDbText text = OdDbText.cast(obj);
                        dumpSimpleTextAttributes(text, childIndent);
                        dumpCommonEntityData(text, childIndent);
                    } else if (obj.isKindOf(OdDbMText.desc())) { // obj instanceof OdDbMText
                        OdDbMText text = OdDbMText.cast(obj);
                        dumpMTextAttributes(text, childIndent);
                        // test callback
                        OdDbMTextFragmentCallback callback = new ExplodeCallback(childIndent);
                        OdGiWorldDraw dr1 = new OdGiWorldDraw();
                        text.explodeFragments(callback, new Long(OdGiWorldDraw.getCPtr(dr1)));
                        dumpCommonEntityData(text, childIndent);
                    } else if (obj.isKindOf(OdDbCircle.desc())) { // obj instanceof OdDbCircle
                        OdDbCircle circle = OdDbCircle.cast(obj);
                        dumpCircleAttributes(circle, childIndent);
                        dumpCommonEntityData(circle, childIndent);
                    } else if (obj.isKindOf(OdDbArc.desc())) { // obj instanceof OdDbCircle
                        OdDbArc arc = OdDbArc.cast(obj);
                        dumpArcAttributes(arc, childIndent);
                        dumpCommonEntityData(arc, childIndent);
                    } else if (obj.isKindOf(OdDbSpline.desc())) { // obj instanceof OdDbSpline
                        writeLine(indent, "OdDbSpline");
                        OdDbSpline spline = OdDbSpline.cast(obj);
                        writeLine(indent, "after cast");
                        dumpSplineAttributes(spline, childIndent);
                        writeLine(indent, "after dumpSpline");
                        dumpCommonEntityData(spline, childIndent);
                        writeLine(indent, "after dumpEntity");
                    } else if (obj.isKindOf(OdDbHatch.desc())) { // obj instanceof OdDbHatch
                        OdDbHatch hatch = OdDbHatch.cast(obj);
                        dumpHatchAttributes(hatch, childIndent);
                        dumpCommonEntityData(hatch, childIndent);
                    } else if (obj.isKindOf(OdDbBlockReference.desc())) { // obj instanceof OdDbBlockReference
                        OdDbBlockReference blockRef = OdDbBlockReference.cast(obj);
                        dumpBlockRefData(blockRef, childIndent);
                        dumpCommonEntityData(blockRef, childIndent);
                    } else {
                        writeLine(childIndent, "!WARNING!", "NOT SUPPORTED YET: " + toString(obj.isA()));
                    }
                    // change related to bug 14152
                    // explicitly delete OdDbObject object
                    obj.delete();
                }
            }
            // changes for bug #14152 second part
            // explicitly delete OdDbObject and blockTable object
            blockTable.delete();
            pId.delete();
        }


        private void dumpLayout(OdDbLayout layout, int indent) {
            writeLine(indent, "Layout Name", _toString(layout.getLayoutName()));
            layout.getBlockTableRecordId();


        }

        void dumpDbInfo(OdDbObject dbObject, int indent) {
            writeLine(indent++, "Type", String.valueOf(dbObject.isA()));
            writeLine(indent, "IsErased", _toString(dbObject.isErased()));
        }

        /************************************************************************/
	/* Dump Block Reference Data                                             */

        /**
         * ********************************************************************
         */
        void dumpBlockRefData(OdDbBlockReference blkRef, int indent) {
            writeLine(indent, "Position", _toString(blkRef.position()));
            writeLine(indent, "Rotation", toDegreeString(blkRef.rotation()));
            writeLine(indent, "Scale Factors", _toString(blkRef.scaleFactors()));
            writeLine(indent, "Normal", _toString(blkRef.normal()));

            /**********************************************************************/
	  /* Dump the attributes                                                */
            /**********************************************************************/
            OdDbObjectIterator iter = blkRef.attributeIterator();
            for (int i = 0; !iter.done(); i++, iter.step()) {
                OdDbAttribute attr = OdDbAttribute.cast(iter.entity());
                if (OdDbAttribute.getCPtr(attr) != 0) {
                    dumpAttributeData(indent, attr, i);
                }
            }
        }

        /************************************************************************/
	/* Dump Attribute data                                                  */

        /**
         * ********************************************************************
         */
        void dumpAttributeData(int indent, OdDbAttribute attr, int i) {
            writeLine(indent++, toString(attr.isA()), _toString(i));
            writeLine(indent, "Handle", toString(attr.getDbHandle()));
            writeLine(indent, "Tag", _toString(attr.tag()));
            writeLine(indent, "Field Length", _toString(attr.fieldLength()));
            writeLine(indent, "Invisible", _toString(attr.isInvisible()));
            writeLine(indent, "Preset", _toString(attr.isPreset()));
            writeLine(indent, "Verifiable", _toString(attr.isVerifiable()));
            writeLine(indent, "Locked in Position", _toString(attr.lockPositionInBlock()));
            writeLine(indent, "Constant", _toString(attr.isConstant()));
            dumpSimpleTextAttributes(attr, indent);
        }


        void dumpHatchAttributes(OdDbHatch hatch, int indent) {
            writeLine(indent, "Hatch Style", toString(hatch.hatchStyle()));
            writeLine(indent, "Hatch Object Type", toString(hatch.hatchObjectType()));
            writeLine(indent, "Is Hatch", _toString(hatch.isHatch()));
            writeLine(indent, "Is Gradient", _toString(!hatch.isGradient()));
            if (hatch.isHatch()) {
                /******************************************************************/
	      /* Dump Hatch Parameters                                          */
                /******************************************************************/
                writeLine(indent, "Pattern Type", toString(hatch.patternType()));
                switch (hatch.patternType()) {
                    case kPreDefined:
                    case kCustomDefined:
                        writeLine(indent, "Pattern Name", _toString(hatch.patternName()));
                        writeLine(indent, "Solid Fill", _toString(hatch.isSolidFill()));
                        if (!hatch.isSolidFill()) {
                            writeLine(indent, "Pattern Angle", toDegreeString(hatch.patternAngle()));
                            writeLine(indent, "Pattern Scale", _toString(hatch.patternScale()));
                        }
                        break;
                    case kUserDefined:
                        writeLine(indent, "Pattern Angle", toDegreeString(hatch.patternAngle()));
                        writeLine(indent, "Pattern Double", _toString(hatch.patternDouble()));
                        writeLine(indent, "Pattern Space", _toString(hatch.patternSpace()));
                        break;
                }
            }
            if (hatch.isGradient()) {
                /******************************************************************/
	      /* Dump Gradient Parameters                                       */
                /******************************************************************/
                writeLine(indent, "Gradient Type", _toString(hatch.gradientType().swigValue())); // print as int to be comparable
                writeLine(indent, "Gradient Name", _toString(hatch.gradientName()));
                writeLine(indent, "Gradient Angle", toDegreeString(hatch.gradientAngle()));
                writeLine(indent, "Gradient Shift", _toString(hatch.gradientShift()));
                writeLine(indent, "Gradient One-Color Mode", _toString(hatch.getGradientOneColorMode()));
                if (hatch.getGradientOneColorMode()) {
                    writeLine(indent, "ShadeTintValue", _toString(hatch.getShadeTintValue()));
                }
                OdCmColorArray colors = new OdCmColorArray();
                OdGeDoubleArray values = new OdGeDoubleArray();
                hatch.getGradientColors(colors, values);
                for (int i = 0; i < (int) colors.size(); i++) {
                    writeLine(indent, toString("Color         %d", i), toString(colors.get(i)));
                    writeLine(indent, toString("Interpolation %d", i), _toString(values.get(i)));
                }
            }

            /********************************************************************/
	    /* Dump Associated Objects                                          */
            /********************************************************************/
            writeLine(indent, "Associated objects", _toString(hatch.associative()));
            OdDbObjectIdArray assocIds = new OdDbObjectIdArray();
            hatch.getAssocObjIds(assocIds);
            int i;
            for (i = 0; i < (int) assocIds.size(); i++) {
                writeLine(indent, "openObject() case2");
                // changes for bug #14152 first part
                // we explicitly create OdDbObject and entity object
                OdDbObject pId = assocIds.get(i).openObject();
                OdDbEntity assoc = OdDbEntity.cast(pId);
                if (OdDbEntity.getCPtr(assoc) != 0)
                    writeLine(indent + 1, toString(assoc.isA()), toString(assoc.getDbHandle()));
                // changes for bug #14152 second part
                // we explicitly delete entity and OdDbObject object
                assoc.delete();
                pId.delete();
            }

            /********************************************************************/
	    /* Dump Seed Points                                                 */
            /********************************************************************/
            writeLine(indent, "Seed points", _toString(hatch.numSeedPoints()));
            for (i = 0; i < hatch.numSeedPoints(); i++) {
                writeLine(indent + 1, toString("Seed point %d", i), _toString(hatch.getSeedPointAt(i)));
            }

            /********************************************************************/
	    /* Dump Loops                                                       */
            /********************************************************************/
            writeLine(indent, "Loops", _toString(hatch.numLoops()));
            for (i = 0; i < hatch.numLoops(); i++) {
                writeLine(indent + 1, toString("Loop %d", i), toLooptypeString((int) hatch.loopTypeAt(i)));

                /******************************************************************/
	      /* Dump Loop                                                      */
                /******************************************************************/
                if ((hatch.loopTypeAt(i) & OdDbHatch.HatchLoopType.kPolyline.swigValue()) != 0) {
                    dumpPolylineType(i, hatch, indent + 2);
                } else {
                    dumedgesType(i, hatch, indent + 2);
                }
                /******************************************************************/
	      /* Dump Associated Objects                                        */
                /******************************************************************/
                if (hatch.associative()) {
                    writeLine(indent + 2, "Associated objects");
                    assocIds.clear();
                    hatch.getAssocObjIdsAt(i, assocIds);
                    for (int j = 0; j < (int) assocIds.size(); j++) {
                        writeLine(indent, "openObject() case3");
                        // changes for bug #14152 first part
                        // we explicitly create OdDbObject and entity object
                        //OdDbEntity assoc = OdDbEntity.cast(assocIds.get(j).openObject());
                        OdDbObject pId = assocIds.get(j).openObject();
                        OdDbEntity assoc = OdDbEntity.cast(pId);
                        if (OdDbEntity.getCPtr(assoc) != 0)
                            writeLine(indent + 3, toString(assoc.isA()), toString(assoc.getDbHandle()));
                        // changes for bug #14152 second part
                        // we explicitly delete entity and OdDbObject object
                        assoc.delete();
                        pId.delete();
                    }
                }
            }

            writeLine(indent, "Elevation", _toString(hatch.elevation()));
            writeLine(indent, "Normal", _toString(hatch.normal()));
        }

        private void dumpPolylineType(int loopIndex, OdDbHatch hatch, int indent) {
            OdGePoint2dArray vertices = new OdGePoint2dArray();
            OdGeDoubleArray bulges = new OdGeDoubleArray();
            hatch.getLoopAt(loopIndex, vertices, bulges);
            boolean hasBulges = (bulges.size() > 0);
            for (int i = 0; i < (int) vertices.size(); i++) {
                writeLine(indent, toString("Vertex %d", i), _toString(vertices.get(i)));
                if (hasBulges) {
                    writeLine(indent + 1, toString("Bulge %d", i), _toString(bulges.get(i)));
                    writeLine(indent + 1, toString("Bulge angle %d", i), toDegreeString(4 * Math.atan(bulges.get(i))));
                }
            }
        }

        /***********************************************************************/
	/* Dump Edge Loop                                                      */

        /**
         * *******************************************************************
         */
        void dumedgesType(int loopIndex, OdDbHatch hatch, int indent) {

            EdgeArray edges = new EdgeArray();
            hatch.getLoopAt(loopIndex, edges);
            //	    writeLine(indent++, "Edges");
            for (int i = 0; i < (int) edges.size(); i++) {
                OdGeCurve2d edge = edges.get(i);
                writeLine(indent, toString("Edge %d", i), _toString(edge.type().name()));
                switch (edge.type()) {
                    case kLineSeg2d:
                        break;
                    case kCircArc2d:
                        dumpCircularArcEdge(indent + 1, edge);
                        break;
                    case kEllipArc2d:
                        dumpEllipticalArcEdge(indent + 1, edge);
                        break;
                    case kNurbCurve2d:
                        dumpNurbCurveEdge(indent + 1, edge);
                        break;
                    default:
                        break;
                }

                /******************************************************************/
	      /* Common Edge Properties                                         */
                /******************************************************************/
                OdGeInterval interval = new OdGeInterval();
                edge.getInterval(interval);
                double[] lower = {0.0};
                double[] upper = {0.0};

                interval.getBounds(lower, upper);
                writeLine(indent + 1, "Start Point", _toString(edge.evalPoint(lower[0])));
                writeLine(indent + 1, "End Point", _toString(edge.evalPoint(upper[0])));
                writeLine(indent + 1, "Closed", _toString(edge.isClosed()));
            }
        }

        /**********************************************************************/
	  /* Dump Circular Arc Edge                                             */

        /**
         * ******************************************************************
         */
        void dumpCircularArcEdge(int indent, OdGeCurve2d edge) {
            //OdGeCircArc2d circleArc = (OdGeCircArc2d)edge;
            OdGeCircArc2d circleArc = new OdGeCircArc2d(OdGeCircArc2d.getCPtr(edge), false);
            writeLine(indent, "Center", _toString(circleArc.center()));
            writeLine(indent, "Radius", _toString(circleArc.radius()));
            writeLine(indent, "Start Angle", toDegreeString(circleArc.startAng()));
            writeLine(indent, "End Angle", toDegreeString(circleArc.endAng()));
            writeLine(indent, "Clockwise", _toString(circleArc.isClockWise()));
        }

        /**********************************************************************/
	  /* Dump Elliptical Arc Edge                                           */

        /**
         * ******************************************************************
         */
        void dumpEllipticalArcEdge(int indent, OdGeCurve2d edge) {
            //OdGeEllipArc2d ellipArc = (OdGeEllipArc2d)edge;
            OdGeEllipArc2d ellipArc = new OdGeEllipArc2d(OdGeEllipArc2d.getCPtr(edge), false);
            writeLine(indent, "Center", _toString(ellipArc.center()));
            writeLine(indent, "Major Radius", _toString(ellipArc.majorRadius()));
            writeLine(indent, "Minor Radius", _toString(ellipArc.minorRadius()));
            writeLine(indent, "Major Axis", _toString(ellipArc.majorAxis()));
            writeLine(indent, "Minor Axis", _toString(ellipArc.minorAxis()));
            writeLine(indent, "Start Angle", toDegreeString(ellipArc.startAng()));
            writeLine(indent, "End Angle", toDegreeString(ellipArc.endAng()));
            writeLine(indent, "Clockwise", _toString(ellipArc.isClockWise()));
        }
        /**********************************************************************/
	  /* Dump NurbCurve Edge                                           */

        /**
         * ******************************************************************
         */
        void dumpNurbCurveEdge(int indent, OdGeCurve2d edge) {

            //OdGeNurbCurve2d nurbCurve = (OdGeNurbCurve2d)edge;
            // should be so, as cast is impossible
            OdGeNurbCurve2d nurbCurve = new OdGeNurbCurve2d(OdGeCurve2d.getCPtr(edge), false);
            int[] degree = {0};
            boolean[] rational = {true};
            boolean[] periodic = {true};
            OdGePoint2dArray ctrlPts = new OdGePoint2dArray();
            OdGeDoubleArray weights = new OdGeDoubleArray();
            OdGeKnotVector knots = new OdGeKnotVector();

            nurbCurve.getDefinitionData(degree, rational, periodic, knots, ctrlPts, weights);
            writeLine(indent, "Degree", _toString(degree[0]));
            writeLine(indent, "Rational", _toString(rational[0]));
            writeLine(indent, "Periodic", _toString(periodic[0]));

            writeLine(indent, "Number of Control Points", _toString((int) ctrlPts.size()));
            int i;
            for (i = 0; i < (int) ctrlPts.size(); i++) {
                writeLine(indent, toString("Control Point %d", i), _toString(ctrlPts.get(i)));
            }
            writeLine(indent, "Number of Knots", _toString(knots.length()));
            for (i = 0; i < knots.length(); i++) {
                writeLine(indent, toString("Knot %d", i), _toString(knots.getItem(i)));
            }

            if (rational[0]) {
                writeLine(indent, "Number of Weights", _toString((int) weights.size()));
                for (i = 0; i < (int) weights.size(); i++) {
                    writeLine(indent, toString("Weight %d", i), _toString(weights.get(i)));
                }
            }
        }

        void dumpArcAttributes(OdDbArc arc, int indent) {
            writeLine(indent, "Center", _toString(arc.center()));
            writeLine(indent, "Radius", _toString(arc.radius()));
            writeLine(indent, "Start Angle", toDegreeString(arc.startAngle()));
            writeLine(indent, "End Angle", toDegreeString(arc.endAngle()));
            writeLine(indent, "Normal", _toString(arc.normal()));
            writeLine(indent, "Thickness", _toString(arc.thickness()));
            dumpCurveData(arc, indent);

        }

        void dumpSplineAttributes(OdDbSpline spline, int indent) {
            int[] degree = {0};
            boolean[] rational = {false};
            boolean[] closed = {false};
            boolean[] periodic = {false};
            OdGePoint3dArray controlPoints = new OdGePoint3dArray();
            OdGeDoubleArray knots = new OdGeDoubleArray();
            OdGeDoubleArray weights = new OdGeDoubleArray();
            double[] controlPtTol = {0};
            double[] knotTol = {0};

            writeLine(indent, "before nurbsdata");
            spline.getNurbsData(degree, rational, closed, periodic, controlPoints, knots, weights, controlPtTol, knotTol);
            writeLine(indent, "after nurbsdata");

            writeLine(indent, "GetNurbsData");
            writeLine(indent, "Degree", _toString(degree[0]));
            dumpCurveData(spline, indent);

        }

        void dumpCircleAttributes(OdDbCircle circle, int indent) {
            writeLine(indent, "Center", _toString(circle.center()));
            writeLine(indent, "Radius", _toString(circle.radius()));
            writeLine(indent, "Diameter", _toString(2 * circle.radius()));
            writeLine(indent, "Normal", _toString(circle.normal()));
            writeLine(indent, "Thickness", _toString(circle.thickness()));

            dumpCurveData(circle, indent);
        }


        void dumpCurveData(OdDbCurve curve, int indent) {

            OdGePoint3d startPoint = new OdGePoint3d();
            if (OdResult.eOk == curve.getStartPoint(startPoint)) {
                writeLine(indent, "Start Point", _toString(startPoint));
            }

            OdGePoint3d endPoint = new OdGePoint3d();
            if (OdResult.eOk == curve.getEndPoint(endPoint)) {
                writeLine(indent, "End Point", _toString(endPoint));
            }
            writeLine(indent, "Closed", _toString(curve.isClosed()));
            writeLine(indent, "Periodic", _toString(curve.isPeriodic()));

            double[] area = {0.0};
            if (OdResult.eOk == curve.getArea(area)) {
                writeLine(indent, "Area", _toString(area[0]));
            }

        }


        void dumpSimpleLineAttributes(OdDbLine line, int indent) {
            writeLine(indent, "Normal", _toString(line.normal()));
            writeLine(indent, "Thickness", _toString(line.thickness()));
        }

        void dumpSimplePolylineAttributes(OdDbPolyline poly, int indent) {
            writeLine(indent, "Has Width", _toString(poly.hasWidth()));
            if (!poly.hasWidth()) {
                writeLine(indent, "Constant Width", _toString(poly.getConstantWidth()));
            }
            writeLine(indent, "Has Bulges", _toString(poly.hasBulges()));
            writeLine(indent, "Elevation", _toString(poly.elevation()));
            writeLine(indent, "Normal", _toString(poly.normal()));
            writeLine(indent, "Thickness", _toString(poly.thickness()));
        }

        void dumpPolylineVertices(OdDbPolyline poly, int indent) {
            for (int i = 0; i < (int) poly.numVerts(); i++) {
                writeLine(indent, toString("Vertex %d", i));
                writeLine(indent + 1, "Segment Type", toString(poly.segType(i)));
                OdGePoint3d pt = new OdGePoint3d();
                poly.getPointAt(i, pt);
                writeLine(indent + 1, "Point", _toString(pt));
                if (poly.hasWidth()) {
                    double[] startWidth = {0.0};
                    double[] endWidth = {0.0};
                    ;
                    poly.getWidthsAt(i, startWidth, endWidth);
                    writeLine(indent, "Start Width ", _toString(startWidth[0]));
                    writeLine(indent, "End Width ", _toString(endWidth[0]));
                }
                if (poly.hasBulges()) {
                    writeLine(indent, "Bulge", _toString(poly.getBulgeAt(i)));
                    if (poly.segType(i) == OdDbPolyline.SegType.kArc) {
                        writeLine(indent, "Bulge Angle", toDegreeString(4 * Math.atan(poly.getBulgeAt(i))));
                    }
                }
            }
        }

        void dumpMTextAttributes(OdDbMText text, int indent) {
            writeLine(indent, "Contents", _toString(text.contents()));
            writeLine(indent, "Location", _toString(text.location()));
            writeLine(indent, "Height", _toString(text.textHeight()));
            writeLine(indent, "Rotation", toDegreeString(text.rotation()));
            writeLine(indent, "Text Style", toString(text.textStyle()));
            writeLine(indent, "Attachment", toString(text.attachment()));
            writeLine(indent, "Background Fill On", _toString(text.backgroundFillOn()));
            writeLine(indent, "Background Fill Color", toString(text.getBackgroundFillColor()));
            writeLine(indent, "Background Scale Factor", _toString(text.getBackgroundScaleFactor()));
            writeLine(indent, "Background Transparency Method", String.valueOf(text.getBackgroundTransparency()));
            writeLine(indent, "X-Direction", _toString(text.direction()));
            writeLine(indent, "Flow Direction", toString(text.flowDirection()));
            writeLine(indent, "Horizontal Mode", toString(text.horizontalMode()));
            writeLine(indent, "Vertical Mode", toString(text.verticalMode()));
            writeLine(indent, "Width", _toString(text.width()));
            writeLine(indent, "Actual Height", _toString(text.actualHeight()));
            writeLine(indent, "Actual Width", _toString(text.actualWidth()));

            OdGePoint3dArray points = new OdGePoint3dArray();
            text.getBoundingPoints(points);
            writeLine(indent, "TL Bounding Point", _toString(points.get(0)));
            writeLine(indent, "TR Bounding Point", _toString(points.get(1)));
            writeLine(indent, "BL Bounding Point", _toString(points.get(2)));
            writeLine(indent, "BR Bounding Point", _toString(points.get(3)));
            writeLine(indent, "Normal", _toString(text.normal()));

            /********************************************************************/
		    /* Dump Indents and Tabs                                            */
            /********************************************************************/
            OdDbMTextIndents indents = new OdDbMTextIndents();
            text.getParagraphsIndent(indents);
            for (int i = 0; i < (int) indents.size(); i++) {
                writeLine(indent, "Indent", _toString(i));
                writeLine(indent + 1, "First Line", _toString(indents.get(i).getFirstLineInd()));
                writeLine(indent + 1, "Paragraph", _toString(indents.get(i).getParagraphInd()));
                String rightString = "";
                for (int j = 0; j < (int) indents.get(i).getTabs().size(); j++) {
                    if (j != 0) {
                        rightString += ", ";
                    }
                    rightString += _toString(indents.get(i).getTabs().get(j));
                }
                if (rightString != "") {
                    writeLine(indent + 1, "Tabs", rightString);
                }
            }
        }

        void dumpSimpleTextAttributes(OdDbText text, int indent) {
            writeLine(indent, "Text String", _toString(text.textString()));
            writeLine(indent, "Text Position", _toString(text.position()));
            writeLine(indent, "Default Alignment", _toString(text.isDefaultAlignment()));
            writeLine(indent, "Alignment Point", _toString(text.alignmentPoint()));
            writeLine(indent, "Height", _toString(text.height()));
            writeLine(indent, "Rotation", toDegreeString(text.rotation()));
            writeLine(indent, "Horizontal Mode", toString(text.horizontalMode()));
            writeLine(indent, "Vertical Mode", toString(text.verticalMode()));
            writeLine(indent, "Mirrored in X", _toString(text.isMirroredInX()));
            writeLine(indent, "Mirrored in Y", _toString(text.isMirroredInY()));
            writeLine(indent, "Oblique", toDegreeString(text.oblique()));
            writeLine(indent, "Text Style", toString(text.textStyle()));
            if (text.textStyle() != null) {
                OdDbTextStyleTableRecord style = OdDbTextStyleTableRecord.cast(text.textStyle().safeOpenObject());
                String[] typeface = {""};
                boolean[] bold = {false};
                boolean[] italic = {false};
                int[] charset = {0};
                int[] pitchAndFamily = {0};
                style.font(typeface, bold, italic, charset, pitchAndFamily);
                writeLine(indent + 2, "typeface", _toString(typeface[0]));
                writeLine(indent + 2, "bold", _toString(bold[0]));
                writeLine(indent + 2, "italic", _toString(italic[0]));
                writeLine(indent + 2, "charset", _toString(charset[0]));
                writeLine(indent + 2, "pitchAndFamily", _toString(pitchAndFamily[0]));
            }


            writeLine(indent, "Width Factor", _toString(text.widthFactor()));

            /*********************************************************************/
	  /* Dump Bounding Points                                               */
            /**********************************************************************/
            OdGePoint3dArray points = new OdGePoint3dArray();
            text.getBoundingPoints(points);
            writeLine(indent, "TL Bounding Point", _toString(points.get(0)));
            writeLine(indent, "TR Bounding Point", _toString(points.get(1)));
            writeLine(indent, "BL Bounding Point", _toString(points.get(2)));
            writeLine(indent, "BR Bounding Point", _toString(points.get(3)));
            writeLine(indent, "Normal", _toString(text.normal()));
            writeLine(indent, "Thickness", _toString(text.thickness()));

        }


        /************************************************************************/
	/* Dump data common to all entities                                     */

        /**
         * ********************************************************************
         */
        void dumpCommonEntityData(OdDbEntity entity, int indent) {
            OdGeExtents3d extents = new OdGeExtents3d();
            if (OdResult.eOk == entity.getGeomExtents(extents)) {
                writeLine(indent, "Min Extents", _toString(extents.minPoint()));
                writeLine(indent, "Max Extents", _toString(extents.maxPoint()));
            }
            writeLine(indent, "Layer", _toString(entity.layer()));
            writeLine(indent, "Color Index", _toString(entity.colorIndex()));
            writeLine(indent, "Color", toString(entity.color()));
            writeLine(indent, "Linetype", _toString(entity.linetype()));
            writeLine(indent, "LTscale", _toString(entity.linetypeScale()));
            writeLine(indent, "Lineweight", toStringLW(entity.lineWeight()));
            writeLine(indent, "Plot Style", _toString(entity.plotStyleName()));
            writeLine(indent, "Transparency Method", toString(entity.transparency()));
            writeLine(indent, "Visibility", toString(entity.visibility()));
            writeLine(indent, "Planar", _toString(entity.isPlanar()));

	 /* OdGePlane plane;
	  OdDb::Planarity planarity = OdDb::kNonPlanar;
	  entity.getPlane(plane, planarity);
	  writeLine(indent, "Planarity",            toString(planarity));
	  if (entity.isPlanar()) 
	  {
		OdGePoint3d origin;
		OdGeVector3d uAxis;
		OdGeVector3d vAxis;
		plane.get(origin, uAxis, vAxis);
		writeLine(indent+1, "Origin",           toString(origin));
		writeLine(indent+1, "u-Axis",           toString(uAxis));
		writeLine(indent+1, "v-Axis",           toString(vAxis));
	  }*/

        }

        //======================================================
        // writeLine Methods
        //======================================================
        private void writeLine() {
            writeLine(0, "", "");
        }

        private void writeLine(int indent, String left) {
            writeLine(indent, left, "");
        }

        private void writeLine(int indent, String left, String right) {
            String spaces = new String("                                                            ");
            String leader = new String(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ");

            final int tabSize = 2;
            final int colWidth = 38;

            String buffer = "";

            /**********************************************************************/
		/* Indent leftString with spaces characters                           */
            /**********************************************************************/
            left = spaces.substring(0, tabSize * indent) + left;

            /**********************************************************************/
		/* If rightString is not specified, just output the indented          */
		/* leftString. Otherwise, fill the space between leftString and       */
		/* rightString with leader characters.                                */
            /**********************************************************************/
            if (right.isEmpty()) {
                buffer = left;
            } else {

                if (colWidth > left.length()) {
                    buffer = left + leader.substring(left.length(), colWidth) + right;
                } else {
                    buffer = left + ' ' + right;
                }
            }

            System.out.println(buffer);
            writer.println(buffer);
        }

        //======================================================
        // writeLine Methods
        //======================================================
        private void writeLineFile() {
            writeLineFile(0, "", "");
        }

        private void writeLineFile(int indent, String left) {
            writeLineFile(indent, left, "");
            //bw.writeLine(content);
        }

        private void writeLineFile(int indent, String left, String right) {
            String spaces = new String("                                                            ");
            String leader = new String(". . . . . . . . . . . . . . . . . . . . . . . . . . . . . . ");

            final int tabSize = 2;
            final int colWidth = 38;

            String buffer = "";

            /**********************************************************************/
		/* Indent leftString with spaces characters                           */
            /**********************************************************************/
            left = spaces.substring(0, tabSize * indent) + left;

            /**********************************************************************/
		/* If rightString is not specified, just output the indented          */
		/* leftString. Otherwise, fill the space between leftString and       */
		/* rightString with leader characters.                                */
            /**********************************************************************/
            if (right.isEmpty()) {
                buffer = left;
            } else {

                if (colWidth > left.length()) {
                    buffer = left + leader.substring(left.length(), colWidth) + right;
                } else {
                    buffer = left + ' ' + right;
                }
            }

            //System.out.println(buffer);
            writer.println(buffer);

        }

        //======================================================
        // toString Methods
        //======================================================

        private String toString(OdRxClass val) {
            return "<" + val.name() + ">";
        }

        private String _toString(String val) {
            return val;
        }

        private String _toString(boolean val) {
            return val ? "true" : "false";
        }

        private String _toString(double val) {
            return "" + val;
        }

        private String _toString(int val) {
            return "" + val;
        }

        private String toString(String fmt, int val) {
            return String.format(fmt, val);
        }


        private String toString(OdCmColor val) {
            return val.toString() + " r:" + val.red() + " g:" + val.green() + " b:" + val.blue();
        }

        private String toStringLW(int val) {
            String retVal = "???";
            switch (val) {
                case LineWeight.kLnWt000:
                    retVal = ("kLnWt000");
                    break;
                case LineWeight.kLnWt005:
                    retVal = ("kLnWt005");
                    break;
                case LineWeight.kLnWt009:
                    retVal = ("kLnWt009");
                    break;
                case LineWeight.kLnWt013:
                    retVal = ("kLnWt013");
                    break;
                case LineWeight.kLnWt015:
                    retVal = ("kLnWt015");
                    break;
                case LineWeight.kLnWt018:
                    retVal = ("kLnWt018");
                    break;
                case LineWeight.kLnWt020:
                    retVal = ("kLnWt020");
                    break;
                case LineWeight.kLnWt025:
                    retVal = ("kLnWt025");
                    break;
                case LineWeight.kLnWt030:
                    retVal = ("kLnWt030");
                    break;
                case LineWeight.kLnWt035:
                    retVal = ("kLnWt035");
                    break;
                case LineWeight.kLnWt040:
                    retVal = ("kLnWt040");
                    break;
                case LineWeight.kLnWt050:
                    retVal = ("kLnWt050");
                    break;
                case LineWeight.kLnWt053:
                    retVal = ("kLnWt053");
                    break;
                case LineWeight.kLnWt060:
                    retVal = ("kLnWt060");
                    break;
                case LineWeight.kLnWt070:
                    retVal = ("kLnWt070");
                    break;
                case LineWeight.kLnWt080:
                    retVal = ("kLnWt080");
                    break;
                case LineWeight.kLnWt090:
                    retVal = ("kLnWt090");
                    break;
                case LineWeight.kLnWt100:
                    retVal = ("kLnWt100");
                    break;
                case LineWeight.kLnWt106:
                    retVal = ("kLnWt106");
                    break;
                case LineWeight.kLnWt120:
                    retVal = ("kLnWt120");
                    break;
                case LineWeight.kLnWt140:
                    retVal = ("kLnWt140");
                    break;
                case LineWeight.kLnWt158:
                    retVal = ("kLnWt158");
                    break;
                case LineWeight.kLnWt200:
                    retVal = ("kLnWt200");
                    break;
                case LineWeight.kLnWt211:
                    retVal = ("kLnWt211");
                    break;
                case LineWeight.kLnWtByLayer:
                    retVal = ("kLnWtByLayer");
                    break;
                case LineWeight.kLnWtByBlock:
                    retVal = ("kLnWtByBlock");
                    break;
                case LineWeight.kLnWtByLwDefault:
                    retVal = ("kLnWtByLwDefault");
                    break;
            }
            return retVal;
        }

        public String toString(OdDbObjectId val) {
            if (val.isNull()) {
                return "Null";
            }

            if (val.isErased()) {
                return "Erased";
            }

            /**********************************************************************/
	  /* Open the object                                                    */
            /**********************************************************************/
            OdDbObject object = OdDbObject.cast(val.safeOpenObject());

            /**********************************************************************/
	  /* Return the name of an OdDbSymbolTableRecord                        */
            /**********************************************************************/
            if (object.isKindOf(OdDbSymbolTableRecord.desc())) {
                OdDbSymbolTableRecord pSTR = OdDbSymbolTableRecord.cast(object);
                return pSTR.getName();
            }

            /**********************************************************************/
	  /* Return the name of an OdDbMlineStyle                               */
            /**********************************************************************/
	  /*if (object.isKindOf(OdDbMlineStyle.desc()))
	  {
		OdDbMlineStylePtr pStyle = object;
		return pStyle.name(); 
	  }*/

            /**********************************************************************/
	  /* Return the name of a PlotStyle                                      */
            /**********************************************************************/
	  /*if (object.isKindOf(OdDbPlaceHolder.desc()))
	  {
		OdDbDictionaryPtr pDictionary = val.database().getPlotStyleNameDictionaryId().safeOpenObject(); 
		String plotStyleName = pDictionary.nameAt(val);
		return plotStyleName; 
	  } */

            /**********************************************************************/
	  /* Return the name of an OdDbMaterial                                 */
            /**********************************************************************/
	  /*if (object.isKindOf(OdDbMaterial.desc()))
	  {
		OdDbMaterialPtr pMaterial = object;
		return pMaterial.name(); 
	  } */

            /**********************************************************************/
	  /* We don't know what it is, so return the description of the object  */
	  /* object specified by the ObjectId                                   */
            /**********************************************************************/
            return toString(object.isA());
        }


        /************************************************************************/
	/* Convert the specified value to an OdGeScale3d string                 */

        /**
         * ********************************************************************
         */
        String _toString(OdGeScale3d val) {
            return String.format("[%.1f %.1f %.1f]", val.getSx(), val.getSy(), val.getSz());

        }

        /************************************************************************/
	/* Convert the specified value to an OdGePoint2d string                 */

        /**
         * ********************************************************************
         */
        String _toString(OdGePoint2d val) {
            return String.format("[%.1f %.1f]", val.getX(), val.getY());
        }

        /************************************************************************/
	/* Convert the specified value to an OdGePoint3d string                 */

        /**
         * ********************************************************************
         */
        public String _toString(OdGePoint3d val) {
            return String.format("[%.1f %.1f %.1f]", val.getX(), val.getY(), val.getZ());
        }

        /************************************************************************/
	/* Convert the specified value to an OdGeVector2d string                */

        /**
         * ********************************************************************
         */
        public String _toString(OdGeVector2d val) {
            return String.format("[%.1f %.1f]", val.getX(), val.getY());
        }


        /************************************************************************/
	/* Convert the specified value to an OdGeVector3d string                */

        /**
         * ********************************************************************
         */
        public String _toString(OdGeVector3d val) {
            return String.format("[%.1f %.1f %.1f]", val.getX(), val.getY(), val.getZ());
        }

        public String toString(Visibility val) {
            String retVal = "???";
            switch (val) {
                case kVisible:
                    retVal = "kVisible";
                    break;
                case kInvisible:
                    retVal = "kInvisible";
                    break;
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an OdCmEntityColor string             */
	/*                                                                      */
	/* Note: isByACI() returns true for ACI values of 0 (ByBlock),          */
	/* 7 (ByForeground), 256 (ByLayer), and 257 (None).                     */

        /**
         * ********************************************************************
         */
        String _toString(OdCmEntityColor val) {
            String retVal = "???";
            if (val.isByLayer()) {
                retVal = "ByLayer";
            } else if (val.isByBlock()) {
                retVal = "ByBlock";
            } else if (val.isForeground()) {
                retVal = "Foreground";
            } else if (val.isNone()) {
                retVal = "None";
            } else if (val.isByACI()) {
                retVal = toString("ACI %d", val.colorIndex());
            } else if (val.isByColor()) {
                retVal = "ByColor r" + _toString(val.red()) + ":g"
                        + _toString(val.green()) + ":b" + _toString(val.blue());
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an OdCmTransparency string            */

        /**
         * ********************************************************************
         */
        public String toString(OdCmTransparency val) {
            String retVal = "???";
            if (val.isByLayer()) {
                retVal = "ByLayer";
            } else if (val.isByBlock()) {
                retVal = "ByBlock";
            } else if (val.isClear()) {
                retVal = "Clear";
            } else if (val.isSolid()) {
                retVal = "Solid";
            } else if (val.isByAlpha()) {
                retVal = toString("ByAlpha %d", (int) val.alpha());
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an OdDbPolyline::SegType string       */

        /**
         * ********************************************************************
         */
        public String toString(OdDbPolyline.SegType val) {
            String retVal = "???";
            switch (val) {
                case kArc:
                    retVal = "kArc";
                    break;
                case kCoincident:
                    retVal = "kCoincident";
                    break;
                case kEmpty:
                    retVal = "kEmpty";
                    break;
                case kLine:
                    retVal = "kLine";
                    break;
                case kPoint:
                    retVal = "kPoint";
                    break;
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an OdDb::TextHorzMode string          */

        /**
         * ********************************************************************
         */
        public String toString(TextHorzMode val) {
            String retVal = "???";
            switch (val) {
                case kTextLeft:
                    retVal = "kTextLeft";
                    break;
                case kTextCenter:
                    retVal = "kTextCenter";
                    break;
                case kTextRight:
                    retVal = "kTextRight";
                    break;
                case kTextAlign:
                    retVal = "kTextAlign";
                    break;
                case kTextMid:
                    retVal = "kTextMid";
                    break;
                case kTextFit:
                    retVal = "kTextFit";
                    break;
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an OdDb::TextVertMode string          */

        /**
         * ********************************************************************
         */
        public String toString(TextVertMode val) {
            String retVal = "???";
            switch (val) {
                case kTextBase:
                    retVal = "kTextBase";
                    break;
                case kTextBottom:
                    retVal = "kTextBottom";
                    break;
                case kTextVertMid:
                    retVal = "kTextVertMid";
                    break;
                case kTextTop:
                    retVal = "kTextTop";
                    break;
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an OdDb::HatchPatternType string      */

        /**
         * ********************************************************************
         */
        String toString(HatchPatternType val) {
            String retVal = "???";
            switch (val) {
                case kPreDefined:
                    retVal = "kPreDefined";
                    break;
                case kUserDefined:
                    retVal = "kUserDefined";
                    break;
                case kCustomDefined:
                    retVal = "kCustomDefined";
                    break;
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an OdDb::HatchObjectType string       */

        /**
         * ********************************************************************
         */
        String toString(HatchObjectType val) {
            String retVal = "???";
            switch (val) {
                case kHatchObject:
                    retVal = "kHatchObject";
                    break;
                case kGradientObject:
                    retVal = "kGradientObject";
                    break;
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an OdDb::HatchStyle string            */

        /**
         * ********************************************************************
         */
        String toString(HatchStyle val) {
            String retVal = "???";
            switch (val) {
                case kNormal:
                    retVal = "kNormal";
                    break;
                case kOuter:
                    retVal = "kOuter";
                    break;
                case kIgnore:
                    retVal = "kIgnore";
                    break;
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an OdDbHandle string                  */

        /**
         * ********************************************************************
         */
        String toString(OdDbHandle val) {
            return "[" + val.ascii() + "]";
        }

        /************************************************************************/
	/* Convert the specified value to an AttachmentPoint string  */

        /**
         * ********************************************************************
         */
        String toString(AttachmentPoint val) {
            String retVal = "???";
            switch (val) {
                case kTopLeft:
                    retVal = "kTopLeft";
                    break;
                case kTopCenter:
                    retVal = "kTopCenter";
                    break;
                case kTopRight:
                    retVal = "kTopRight";
                    break;
                case kMiddleLeft:
                    retVal = "kMiddleLeft";
                    break;
                case kMiddleCenter:
                    retVal = "kMiddleCenter";
                    break;
                case kMiddleRight:
                    retVal = "kMiddleRight";
                    break;
                case kBottomLeft:
                    retVal = "kBottomLeft";
                    break;
                case kBottomCenter:
                    retVal = "kBottomCenter";
                    break;
                case kBottomRight:
                    retVal = "kBottomRight";
                    break;
                case kBaseLeft:
                    retVal = "kBaseLeft";
                    break;
                case kBaseCenter:
                    retVal = "kBaseCenter";
                    break;
                case kBaseRight:
                    retVal = "kBaseRight";
                    break;
                case kBaseAlign:
                    retVal = "kBaseAlign";
                    break;
                case kBottomAlign:
                    retVal = "kBottomAlign";
                    break;
                case kMiddleAlign:
                    retVal = "kMiddleAlign";
                    break;
                case kTopAlign:
                    retVal = "kTopAlign";
                    break;
                case kBaseFit:
                    retVal = "kBaseFit";
                    break;
                case kBottomFit:
                    retVal = "kBottomFit";
                    break;
                case kMiddleFit:
                    retVal = "kMiddleFit";
                    break;
                case kTopFit:
                    retVal = "kTopFit";
                    break;
                case kBaseMid:
                    retVal = "kBaseMid";
                    break;
                case kBottomMid:
                    retVal = "kBottomMid";
                    break;
                case kMiddleMid:
                    retVal = "kMiddleMid";
                    break;
                case kTopMid:
                    retVal = "kTopMid";
                    break;
            }
            return retVal;
        }

        /************************************************************************/
	/* Convert the specified value to an FlowDirection string    */

        /**
         * ********************************************************************
         */
        String toString(FlowDirection val) {
            String retVal = "???";
            switch (val) {
                case kLtoR:
                    retVal = "kLtoR";
                    break;
                case kRtoL:
                    retVal = "kRtoL";
                    break;
                case kTtoB:
                    retVal = "kTtoB";
                    break;
                case kBtoT:
                    retVal = "kBtoT";
                    break;
                case kByStyle:
                    retVal = "kByStyle";
                    break;
            }
            return retVal;
        }


        /************************************************************************/
	/* Convert the specified value to a LoopType string                     */

        /**
         * ********************************************************************
         */
        String toLooptypeString(int loopType) {
            String retVal = "";
            if ((loopType & HatchLoopType.kExternal.swigValue()) != 0)
                retVal += " | kExternal";

            if ((loopType & HatchLoopType.kPolyline.swigValue()) != 0)
                retVal += " | kPolyline";

            if ((loopType & HatchLoopType.kDerived.swigValue()) != 0)
                retVal += " | kDerived";

            if ((loopType & HatchLoopType.kTextbox.swigValue()) != 0)
                retVal += " | kTextbox";

            if ((loopType & HatchLoopType.kOutermost.swigValue()) != 0)
                retVal += " | kOutermost";

            if ((loopType & HatchLoopType.kNotClosed.swigValue()) != 0)
                retVal += " | kNotClosed";

            if ((loopType & HatchLoopType.kSelfIntersecting.swigValue()) != 0)
                retVal += " | kSelfIntersecting";

            if ((loopType & HatchLoopType.kTextIsland.swigValue()) != 0)
                retVal += " | kTextIsland";

            if ((loopType & HatchLoopType.kDuplicate.swigValue()) != 0)
                retVal += " | kDuplicate";

            return retVal.isEmpty() ? "kDefault" : retVal.substring(3);
        }

        /************************************************************************/
	/* Convert the specified value to a degree string                       */

        /**
         * ********************************************************************
         */
        public String toDegreeString(double val) {
            return Double.toString(val * 180.0 / Math.PI) + "d";
        }


        class ExplodeCallback extends OdDbMTextFragmentCallback {
            private final int childIndent;

            public ExplodeCallback(int childIndent) {
                super();
                this.childIndent = childIndent;
            }

            @Override
            public int action(OdDbMTextFragment frag) {


                writeLine();

                writeLine(childIndent, "===========FRAGMENT START===========");

                writeLine(childIndent, "location", _toString(frag.getLocation()));

                writeLine(childIndent, "text", _toString(frag.getText()));

                writeLine(childIndent, "font", _toString(frag.getFont()));
                writeLine(childIndent, "bigfont", _toString(frag.getBigfont()));

                writeLine(childIndent, "extents", _toString(frag.getExtents()));
                writeLine(childIndent, "capsheight", _toString(frag.getCapsHeight()));
                writeLine(childIndent, "widthFactor", _toString(frag.getWidthFactor()));
                writeLine(childIndent, "obliqueAngle", _toString(frag.getObliqueAngle()));
                writeLine(childIndent, "trackingFactor", _toString(frag.getTrackingFactor()));

                writeLine(childIndent, "color", _toString(frag.getColor()));
                writeLine(childIndent, "vertical", _toString(frag.getVertical()));

                writeLine(childIndent, "stackTop", _toString(frag.getStackTop()));
                writeLine(childIndent, "stackBottom", _toString(frag.getStackBottom()));

                writeLine(childIndent, "underlined", _toString(frag.getUnderlined()));
                writeLine(childIndent, "overlined", _toString(frag.getOverlined()));
                writeLine(childIndent, "underPoints", _toString(frag.getUnderPoints()));
                writeLine(childIndent, "overPoints", _toString(frag.getOverPoints()));

                writeLine(childIndent, "fontname", _toString(frag.getFontname()));
                writeLine(childIndent, "charset", _toString(frag.getCharset()));
                writeLine(childIndent, "bold", _toString(frag.getBold()));
                writeLine(childIndent, "italic", _toString(frag.getItalic()));

                writeLine(childIndent, "changestyle", _toString(frag.getChangeStyle()));
                writeLine(childIndent, "lineBreak", _toString(frag.getLineBreak()));
                writeLine(childIndent, "newParagraph", _toString(frag.getNewParagraph()));

                writeLine(childIndent, "===========FRAGMENT END===========");


                return 1;
            }

        }

    }

}

