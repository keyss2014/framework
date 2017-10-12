package cn.keyss.test.unitils;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.dbunit.dataset.*;
import org.dbunit.dataset.excel.XlsDataSetWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Created by Administrator on 2016/11/18 0018.
 */
public class XlsDataSet extends AbstractDataSet {
    /**
     * Logger for this class
     */
    private static final Logger logger = LoggerFactory.getLogger(XlsDataSet.class);

    private final OrderedTableNameMap _tables;


    /**
     * Creates a new XlsDataSet object that loads the specified Excel document.
     */
    public XlsDataSet(File file) throws IOException, DataSetException, InvalidFormatException {
        this(new FileInputStream(file));
    }

    /**
     * Creates a new XlsDataSet object that loads the specified Excel document.
     */
    public XlsDataSet(InputStream in) throws IOException, DataSetException, InvalidFormatException {
        _tables = super.createTableNameMap();

        Workbook workbook = WorkbookFactory.create(in);

        int sheetCount = workbook.getNumberOfSheets();
        for (int i = 0; i < sheetCount; i++)
        {
            ITable table = new XlsTable(workbook.getSheetName(i),
                    workbook.getSheetAt(i));
            _tables.add(table.getTableMetaData().getTableName(), table);
        }
    }

    /**
     * Write the specified dataset to the specified Excel document.
     */
    public static void write(IDataSet dataSet, OutputStream out)
            throws IOException, DataSetException
    {
        logger.debug("write(dataSet={}, out={}) - start", dataSet, out);

        new XlsDataSetWriter().write(dataSet, out);
    }


    ////////////////////////////////////////////////////////////////////////////
    // AbstractDataSet class

    protected ITableIterator createIterator(boolean reversed)
            throws DataSetException
    {
        if(logger.isDebugEnabled())
            logger.debug("createIterator(reversed={}) - start", String.valueOf(reversed));

        ITable[] tables = (ITable[]) _tables.orderedValues().toArray(new ITable[0]);
        return new DefaultTableIterator(tables, reversed);
    }
}
