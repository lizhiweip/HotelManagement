import React, { useState, useEffect } from 'react';
import { Table, Input, Button } from 'antd';
import { queryReservationList } from './ReservationService'; 

const { Search } = Input;

const ReservationListPage = () => {
  const [data, setData] = useState([]);
  const [filteredData, setFilteredData] = useState([]);
  const [roomNumSearch, setRoomNumSearch] = useState('');
  const [userNameSearch, setUserNameSearch] = useState('');

  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await queryReservationList(roomNumSearch, userNameSearch);
        setData(response.data);
        setFilteredData(response.data);
      } catch (error) {
        console.error('Error fetching data:', error);
      }
    };

    fetchData();
  }, [roomNumSearch, userNameSearch]);

  const handleSearch = () => {
    const filtered = data.filter(
      (item) =>
        item.roomNum.toLowerCase().includes(roomNumSearch.toLowerCase()) &&
        item.userName.toLowerCase().includes(userNameSearch.toLowerCase())
    );
    setFilteredData(filtered);
  };

  const columns = [
    {
      title: '房间号',
      dataIndex: 'roomNum',
      key: 'roomNum',
    },
    {
      title: '用户姓名',
      dataIndex: 'userName',
      key: 'userName',
    },
    {
      title: '入住时间',
      dataIndex: 'checkInDate',
      key: 'checkInDate',
    },
    {
      title: '退房时间',
      dataIndex: 'checkOutDate',
      key: 'checkOutDate',
    },
  ];

  return (
    <div>
      <Input
        placeholder="通过房间号搜索"
        allowClear
        value={roomNumSearch}
        onChange={(e) => setRoomNumSearch(e.target.value)}
        style={{ width: 200, marginRight: 8, marginBottom: 16 }}
      />
      <Input
        placeholder="通过用户姓名搜索"
        allowClear
        value={userNameSearch}
        onChange={(e) => setUserNameSearch(e.target.value)}
        style={{ width: 200, marginRight: 8, marginBottom: 16 }}
      />
      <Button type="primary" onClick={handleSearch} style={{ marginBottom: 16 }}>
        搜索
      </Button>
      <Table dataSource={filteredData} columns={columns} rowKey="reservationId" />
    </div>
  );
};

export default ReservationListPage;
