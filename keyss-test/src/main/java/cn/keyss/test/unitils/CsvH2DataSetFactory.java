package cn.keyss.test.unitils;

import org.dbunit.dataset.CachedDataSet;
import org.dbunit.dataset.DataSetException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.unitils.dbunit.datasetfactory.DataSetFactory;
import org.unitils.dbunit.util.MultiSchemaDataSet;

import java.io.File;
import java.util.Properties;


/**
 * 基于csv的数据集工厂
 */
public class CsvH2DataSetFactory implements DataSetFactory {
    private static final Logger logger = LoggerFactory.getLogger(SingleCsvProducer.class);

    /**
     * 缺省架构名
     */
    protected String defaultSchemaName;

    @Override
    public void init(Properties configuration, String defaultSchemaName) {
        this.defaultSchemaName = defaultSchemaName;
    }

    @Override
    public MultiSchemaDataSet createDataSet(File... dataSetFiles) {
        MultiSchemaDataSet multiSchemaDataSet = new MultiSchemaDataSet();

        try {
            CachedDataSet csvDataSet = new CachedDataSet(new SingleCsvProducer(dataSetFiles));
            multiSchemaDataSet.setDataSetForSchema("PUBLIC", csvDataSet);
        } catch (DataSetException e) {
            logger.error("createDataSet error", e);
        }

        return multiSchemaDataSet;
    }

    @Override
    public String getDataSetFileExtension() {
        return "csv";
    }
}