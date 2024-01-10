import React, { useEffect, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import { queryRoomTypeList, RoomType } from './service';

const RoomTypePage: React.FC = () => {
  const [data, setData] = useState<RoomType[]>([]);

  const fetchData = async () => {
    try {
      const response = await queryRoomTypeList();
      setData(response.data || []);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  const columns = [
    {
      title: '房间类型',
      dataIndex: 'roomTypeName',
    },
    {
      title: '描述',
      dataIndex: 'description',
    },
    {
      title: '原价',
      dataIndex: 'basePrice',
    },
    {
      title: '现价',
      dataIndex: 'price',
    },
  ];

  return (
    <PageContainer>
      <ProTable
        columns={columns}
        dataSource={data}
        search={false}
        pagination={{ pageSize: 5 }} 
        rowKey="roomTypeId"
      />
    </PageContainer>
  );
};
export default RoomTypePage;