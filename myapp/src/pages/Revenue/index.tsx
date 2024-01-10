import React, { useEffect, useState } from 'react';
import { Input, DatePicker, Table, Row, Col } from 'antd';
import moment from 'moment';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend } from 'recharts';
//import { Bar } from 'bizcharts';

const { RangePicker } = DatePicker;

const RevenueList: React.FC = () => {
  // State variables for the table
  const [tableData, setTableData] = useState([]);
  const [tableRoomNum, setTableRoomNum] = useState('');
  const [tableStartDate, setTableStartDate] = useState<string | null>(null);
  const [tableEndDate, setTableEndDate] = useState<string | null>(null);

  // State variables for the bar chart
  const [chartData, setChartData] = useState([]);
  const [chartRoomNum, setChartRoomNum] = useState('');
  const [chartStartDate, setChartStartDate] = useState<string | null>(null);
  const [chartEndDate, setChartEndDate] = useState<string | null>(null);
  const [chartAsc, setChartAsc] = useState<boolean>(false);
  const [chartNum, setChartNum] = useState<number>(0);

  useEffect(() => {
    fetchTableData();
  }, [tableRoomNum, tableStartDate, tableEndDate]);

  useEffect(() => {
    fetchChartData();
  }, [chartRoomNum, chartStartDate, chartEndDate, chartAsc, chartNum]);

  const fetchTableData = async () => {
    try {
      let url = 'http://localhost:8080/revenue/list';

      if (tableRoomNum !== undefined && tableRoomNum !== null && tableRoomNum !== "") {
        url += `?roomNum=${encodeURIComponent(tableRoomNum)}`;
      }

      if (tableStartDate) {
        const startDateiso = moment(tableStartDate).toISOString();
        url += `${url.includes('?') ? '&' : '?'}startDate=${startDateiso}`;
      }

      if (tableEndDate) {
        const endDateiso = moment(tableEndDate).toISOString();
        url += `${url.includes('?') ? '&' : '?'}endDate=${endDateiso}`;
      }

      console.log("Table URL", url);

      const response = await fetch(url);
      const data = await response.json();
      setTableData(data.data);
    } catch (error) {
      console.error('Error fetching table data:', error);
    }
  };

  const fetchChartData = async () => {
    try {
      let chartUrl = 'http://localhost:8080/revenue/sort';

      if (chartRoomNum !== undefined && chartRoomNum !== null && chartRoomNum !== "") {
        chartUrl += `?roomNum=${encodeURIComponent(chartRoomNum)}`;
      }

      if (chartStartDate) {
        const startDateiso = moment(chartStartDate).toISOString();
        chartUrl += `${chartUrl.includes('?') ? '&' : '?'}startDate=${startDateiso}`;
      }

      if (chartEndDate) {
        const endDateiso = moment(chartEndDate).toISOString();
        chartUrl += `${chartUrl.includes('?') ? '&' : '?'}endDate=${endDateiso}`;
      }
      if (chartAsc) {
        chartUrl += `${chartUrl.includes('?') ? '&' : '?'}asc=${(chartAsc)}`;
      }
      if (chartNum) {
        chartUrl += `${chartUrl.includes('?') ? '&' : '?'}num=${chartNum}`;
      }

   
      console.log("Chart URL", chartUrl);

      const chartResponse = await fetch(chartUrl);
      const chartData = await chartResponse.json();
      setChartData(chartData.data);
    } catch (error) {
      console.error('Error fetching chart data:', error);
    }
  };

  const columns = [
    {
      title: '房间号',
      dataIndex: 'roomNum',
      key: 'roomNum',
    },
    {
      title: '营收总额',
      dataIndex: 'totalAmount',
      key: 'totalAmount',
    },
  ];

  return (
    <div>
      <h1>营收数据</h1>
      <Row gutter={[16, 16]}>
        <Col span={8}>
          <label>表格房间号:</label>
          <Input value={tableRoomNum} onChange={(e) => setTableRoomNum(e.target.value)} />
        </Col>
        <Col span={8}>
          <div>
            <label>表格开始日期:</label>
            <DatePicker value={tableStartDate} onChange={(date) => setTableStartDate(date)} />
          </div>
          <div>
            <label>表格结束日期:</label>
            <DatePicker value={tableEndDate} onChange={(date) => setTableEndDate(date)} />
          </div>
        </Col>
      </Row>
      <Table dataSource={tableData} columns={columns} />

     
      <h2 style={{ marginTop: '20px' }}>营收柱状图</h2>
<Row gutter={[16, 16]} style={{ marginBottom: '20px' }}>
  <Col span={6}>
    <label style={{ display: 'block', marginBottom: '8px' }}>房间号:</label>
    <Input value={chartRoomNum} onChange={(e) => setChartRoomNum(e.target.value)} style={{ width: '80%' }} />
  </Col>
  <Col span={6}>
    <div style={{ display: 'flex', alignItems: 'center', marginBottom: '8px' }}>
      <label style={{ marginRight: '8px' }}>开始日期:</label>
      <DatePicker value={chartStartDate} onChange={(date) => setChartStartDate(date)} style={{ width: '80%' }} />
    </div>
    <div style={{ display: 'flex', alignItems: 'center', marginBottom: '8px' }}>
      <label style={{ marginRight: '8px' }}>结束日期:</label>
      <DatePicker value={chartEndDate} onChange={(date) => setChartEndDate(date)} style={{ width: '80%' }} />
    </div>
  </Col>
  <Col span={6}>
    <div style={{ display: 'flex', alignItems: 'center', marginBottom: '8px' }}>
      <label style={{ marginRight: '8px' }}>是否升序:</label>
      <Input type="checkbox" checked={chartAsc} onChange={(e) => setChartAsc(e.target.checked)} />
    </div>
  </Col>
  <Col span={6}>
    <div style={{ display: 'flex', alignItems: 'center', marginBottom: '8px' }}>
      <label style={{ marginRight: '8px' }}>数量:</label>
      <Input type="number" value={chartNum} onChange={(e) => setChartNum(parseInt(e.target.value, 10))} style={{ width: '80%' }} />
    </div>
  </Col>
</Row>
<BarChart width={600} height={300} data={chartData}>
  <CartesianGrid strokeDasharray="3 3" />
  <XAxis dataKey="roomNum" />
  <YAxis />
  <Tooltip />
  <Legend />
  <Bar dataKey="totalAmount" fill="#8884d8" />
</BarChart>
    </div>
  );
};

export default RevenueList;
