package cn.keyss.test.unitils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unitils.dbunit.datasetfactory.DataSetFactory;
import org.unitils.dbunit.util.MultiSchemaDataSet;

import java.io.File;
import java.util.Properties;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class XlsH2DataSetFactory implements DataSetFactory {
    private static final Logger logger = LoggerFactory.getLogger(XlsH2DataSetFactory.class);

    @Override
    public void init(Properties configuration, String defaultSchemaName) {

    }

    @Override
    public MultiSchemaDataSet createDataSet(File... dataSetFiles) {
        MultiSchemaDataSet multiSchemaDataSet = new MultiSchemaDataSet();

        try {
            for(File f:dataSetFiles){
                XlsDataSet xlsDataSet=new XlsDataSet(f);
                multiSchemaDataSet.setDataSetForSchema("PUBLIC", xlsDataSet);
            }
         } catch (Exception e) {
            logger.error("createDataSet error", e);
        }

        return multiSchemaDataSet;
    }

    @Override
    public String getDataSetFileExtension() {
        return "xls";
    }
}
