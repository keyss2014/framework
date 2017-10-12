package cn.keyss.test.unitils;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultTableMetaData;
import org.dbunit.dataset.ITableMetaData;
import org.dbunit.dataset.common.handlers.IllegalInputCharacterException;
import org.dbunit.dataset.common.handlers.PipelineException;
import org.dbunit.dataset.csv.CsvDataSetWriter;
import org.dbunit.dataset.csv.CsvParser;
import org.dbunit.dataset.csv.CsvParserException;
import org.dbunit.dataset.csv.CsvParserImpl;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.stream.DefaultConsumer;
import org.dbunit.dataset.stream.IDataSetConsumer;
import org.dbunit.dataset.stream.IDataSetProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;


/**
 * 单Csv文件DataSet产生器
 */
public class SingleCsvProducer implements IDataSetProducer {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(SingleCsvProducer.class);
    private static final IDataSetConsumer EMPTY_CONSUMER = new DefaultConsumer();
    private IDataSetConsumer _consumer = EMPTY_CONSUMER;
    private File[] csvFiles;

    public SingleCsvProducer(File... dataSetFiles) {
        this.csvFiles = dataSetFiles;
    }

    @Override
    public void setConsumer(IDataSetConsumer consumer) throws DataSetException {
        logger.debug("setConsumer(consumer) - start");

        _consumer = consumer;
    }

    @Override
    public void produce() throws DataSetException {
        logger.debug("produce() - start");

        _consumer.startDataSet();
        for (File csvFile : csvFiles) {
            try {
                produceFromFile(csvFile);
            } catch (CsvParserException e) {
                throw new DataSetException("error producing dataset for file '" + csvFile.getName() + "'", e);
            } catch (DataSetException e) {
                throw new DataSetException("error producing dataset for file '" + csvFile.getName() + "'", e);
            }
        }
        _consumer.endDataSet();
    }

    private void produceFromFile(File theDataFile) throws DataSetException, CsvParserException {
        logger.debug("produceFromFile(theDataFile={}) - start", theDataFile);

        try {
            CsvParser parser = new CsvParserImpl();
            List readData = parser.parse(theDataFile);
            List readColumns = ((List) readData.get(0));
            Column[] columns = new Column[readColumns.size()];

            for (int i = 0; i < readColumns.size(); i++) {
                columns[i] = new Column((String) readColumns.get(i), DataType.UNKNOWN);
            }

            String tableName = theDataFile.getName().split("-")[0];
            ITableMetaData metaData = new DefaultTableMetaData(tableName, columns);
            _consumer.startTable(metaData);
            for (int i = 1; i < readData.size(); i++) {
                List rowList = (List) readData.get(i);
                Object[] row = rowList.toArray();
                for (int col = 0; col < row.length; col++) {
                    row[col] = row[col].equals(CsvDataSetWriter.NULL) ? null : row[col];
                }
                _consumer.row(row);
            }
            _consumer.endTable();
        } catch (PipelineException e) {
            throw new DataSetException(e);
        } catch (IllegalInputCharacterException e) {
            throw new DataSetException(e);
        } catch (IOException e) {
            throw new DataSetException(e);
        }
    }
}