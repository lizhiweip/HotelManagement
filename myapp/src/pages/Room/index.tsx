import React, { useEffect, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import { Modal, DatePicker, Input, message } from 'antd';
import { RoomDto, saveReservation} from './service';
const { RangePicker } = DatePicker;

const RoomPage: React.FC = () => {
  const [data, setData] = useState<RoomDto[]>([]);
  const [total, setTotal] = useState<number>(0);
  const [loading, setLoading] = useState<boolean>(false);

  const [modalVisible, setModalVisible] = useState<boolean>(false);
  const [selectedRoom, setSelectedRoom] = useState<RoomDto | null>(null);
  const [checkInDate, setCheckInDate] = useState<any>(null);
  const [checkOutDate, setCheckOutDate] = useState<any>(null);
  const [idCardNo, setIdCardNo] = useState<string>('');

  const handleBookClick = (record: RoomDto) => {
    setSelectedRoom(record);
    setModalVisible(true);
  };
  const handleModalOk = async () => {
    if (checkInDate && checkOutDate && checkInDate !== checkOutDate) {
      try {

        const isoCheckInDate = checkInDate.toISOString();
        const isoCheckOutDate = checkOutDate.toISOString();
        const result = await saveReservation(
          selectedRoom!.roomNumber,
          isoCheckInDate,
          isoCheckOutDate,
          idCardNo
        );
       console.log("resultcode:" + result.code);
       if(result.code !== 0){
         message.success(result.data);
       }else{
        message.error(result.msg);
       }
        
      } catch (error) {
        console.error('预定出现错误:', error);
        message.error('预定失败，请再次尝试');
      } finally {
        setModalVisible(false);
      }
    } else {
      message.error('日期有误');
    }
  };
  
  

  const handleModalCancel = () => {
    setModalVisible(false);
  };

  const fetchData = async (params: any) => {
    try {
      setLoading(true);

      const { current, pageSize, status, minPrice, maxPrice, roomTypeName } = params;

      let apiUrl = `http://localhost:8080/room/page?page=${current}&pageSize=${pageSize}`;

      if (status !== undefined) {
        apiUrl += `&status=${status}`;
      }
      if (minPrice !== undefined) {
        apiUrl += `&minPrice=${minPrice}`;
      }
      if (maxPrice !== undefined) {
        apiUrl += `&maxPrice=${maxPrice}`;
      }
      if (roomTypeName !== undefined && roomTypeName !== '') {
        apiUrl += `&roomTypeName1=${encodeURIComponent(roomTypeName)}`;
      }

      console.log('API URL:', apiUrl); 

      const response = await fetch(apiUrl, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status} - ${response.statusText}`);
      }

      const result = await response.json();

      setData(result.data.records || []);
      setTotal(result.data.total || 0);
    } catch (error) {
      console.error('Error fetching data:', error);
    } finally {
      setLoading(false);
    }
  };

  const columns = [
    {
      title: '房间编号',
      dataIndex: 'roomNumber',
      search: false,
    },
    {
      title: '当前状态',
      dataIndex: 'status',
      valueEnum: {
        0: '空闲',
        1: '已预订',
      },
      filters: true,
    },
    {
      title: '描述',
      dataIndex: 'description',
      search: false,
    },
    {
      title: '房间类型',
      dataIndex: 'roomTypeName',
      filters: true,
    },
    {
      title: '价格',
      dataIndex: 'price',
      search: false,
    },
    {
      title: '最低价',
      dataIndex: 'minPrice',
      search: true,
      hideInTable: true,
    },
    {
      title: '最高价',
      dataIndex: 'maxPrice',
      search: true,
      hideInTable: true,
    },
    {
      title: '操作',
      dataIndex: 'operation',
      valueType: 'option',
      render: (_, record) => [
        <a key="book" onClick={() => handleBookClick(record)}>预定</a>,
      ],
    },
  ];
  

  return (
    <PageContainer>
      <ProTable
        columns={columns}
        dataSource={data}
        loading={loading}
        pagination={{
          total,
          showQuickJumper: true,
          showSizeChanger: true,
        }}
        request={(params, sorter, filter) => fetchData({ ...params, sorter, filter })}
        rowKey="roomId"
        search={{
          defaultCollapsed: true,
        }}
      />
      <Modal
        title={`预定房间 ${selectedRoom?.roomNumber}`}
        visible={modalVisible}
        onOk={handleModalOk}
        onCancel={handleModalCancel}
      >
        <div>
          <label>入住日期:</label>
          <DatePicker value={checkInDate} onChange={(date) => setCheckInDate(date)} />
        </div>
        <div>
          <label>退房日期:</label>
          <DatePicker value={checkOutDate} onChange={(date) => setCheckOutDate(date)} />
        </div>
        <div>
          <label>身份证号:</label>
          <Input value={idCardNo} onChange={(e) => setIdCardNo(e.target.value)} />
        </div>
      </Modal>
    </PageContainer>
  );
};

export default RoomPage;