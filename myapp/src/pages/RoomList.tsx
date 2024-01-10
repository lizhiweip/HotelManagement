import React, { useState, useEffect } from 'react';
import { Button, Modal, Form, Input, message, Select} from 'antd';
import ProTable from '@ant-design/pro-table';
import { PlusOutlined } from '@ant-design/icons';
import { saveRoom, deleteRoom, updateRoom, listRooms } from './roomservice'; 
const { Option } = Select;

const RoomListPage = () => {
  const [form] = Form.useForm();
  const [visible, setVisible] = useState(false);
  const [editingRoom, setEditingRoom] = useState(null);
  const [dataSource, setDataSource] = useState([]);
  const [roomTypes, setRoomTypes] = useState([]); 
  const [searchForm] = Form.useForm();

  const columns = [
    {
      title: 'Room ID',
      dataIndex: 'roomId',
      hideInTable: true,
    },
    {
      title: '房间号',
      dataIndex: 'roomNumber',
    },
    {
      title: '状态',
      dataIndex: 'status',
      valueEnum: {
        0: '空闲',
        1: '已预订',
      },
    },
    {
      title: '描述',
      dataIndex: 'description',
    },
    {
      title: '房间类型',
      dataIndex: 'roomTypeName',
    },
    {
      title: '价格',
      dataIndex: 'price',
    },
    {
      title: '操作',
      valueType: 'option',
      render: (_, record) => [
        <a key="edit" onClick={() => handleEdit(record)}>
          编辑
        </a>,
        <a key="delete" onClick={() => handleDelete(record.roomId)}>
          删除
        </a>,
      ],
    },
  ];



  const fetchRoomTypes = async () => {
    try {
      const response = await fetch('http://localhost:8080/roomType/list');
      const result = await response.json();
      setRoomTypes(result.data);
    } catch (error) {
      console.error('Error fetching room types:', error);
    }
  };

  const fetchData = async (params) => {
    try {
      const result = await listRooms(params);
      setDataSource(result.data);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  useEffect(() => {
    fetchData();
    fetchRoomTypes();
  }, []);

  const handleAdd = () => {
    setEditingRoom(null);
    setVisible(true);
  };

  const handleEdit = (record) => {
    setEditingRoom(record);
    form.setFieldsValue(record);
    setVisible(true);
  };

  const handleDelete = async (roomId) => {
    try {
      await deleteRoom(roomId);
      message.success('Room deleted successfully!');
      fetchData();
    } catch (error) {
      console.error('Error deleting room:', error);
      message.error('Failed to delete room. Please try again.');
    }
  };

  const handleModalOk = async () => {
    try {
      const values = await form.validateFields();
      const roomTypeName = form.getFieldValue('roomTypeName');
      console.log("11111111",roomTypeName);
      
      if (!values.roomTypeName) {
        throw new Error('Please select a room type');
      }
      const roomData = {
        ...values,
        roomTypeName, 
      };
      if (editingRoom) {
        await updateRoom({ ...editingRoom, ...values });
      } else {
        await saveRoom(values);
      }
      message.success('Room saved successfully!');
      fetchData();
      setVisible(false);
      form.resetFields();
    } catch (error) {
      console.error('Error saving room:', error);
      message.error('Failed to save room. Please try again.');
    }
  };

  const handleModalCancel = () => {
    setVisible(false);
    form.resetFields();
  };
  const handleSearch = () => {
    const values = searchForm.getFieldsValue();
    fetchData(values);
  };

  return (
    <div>
      <Form form={searchForm} layout="inline" style={{ marginBottom: 16 }}>
        <Form.Item name="roomNumber" label="房间号">
          <Input placeholder="请输入房间号" />
        </Form.Item>
        <Form.Item name="status" label="状态">
          <Select placeholder="请选择房间状态">
            <Option value={0}>空闲</Option>
            <Option value={1}>不空闲</Option>
            <Option value={-1}>维护中</Option>
          </Select>
        </Form.Item>
        <Form.Item name="roomTypeName" label="房间类型名">
          <Select placeholder="请选择房间类型名">
            {roomTypes.map((roomType) => (
              <Option key={roomType.roomTypeId} value={roomType.roomTypeName}>
                {roomType.roomTypeName}
              </Option>
            ))}
          </Select>
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={handleSearch}>
            查询
          </Button>
        </Form.Item>
      </Form>

      <ProTable
        columns={columns}
        dataSource={dataSource}
        rowKey="roomId"
        search={false}
        dateFormatter="string"
        headerTitle="房间列表"
        toolBarRender={() => [
          <Button key="button" icon={<PlusOutlined />} type="primary" onClick={handleAdd}>
            添加房间
          </Button>,
        ]}
      />

      <Modal
        title={editingRoom ? 'Edit Room' : '添加房间'}
        visible={visible}
        onOk={handleModalOk}
        onCancel={handleModalCancel}
      >
        <Form form={form} layout="vertical">
          <Form.Item name="roomNumber" label="房间号" rules={[{ required: true, message: '请输入房间号' }]}>
            <Input />
          </Form.Item>
          <Form.Item name="roomTypeName" label="房间类型名" rules={[{ required: true, message: '请选择房间类型名' }]}>
          <Select>
              {roomTypes.map((roomType) => (
                <Option key={roomType.roomTypeId} value={roomType.roomTypeName}>
                  {roomType.roomTypeName}
                </Option>
              ))}
            </Select>
          </Form.Item>
          <Form.Item name="status" label="状态" rules={[{ required: true, message: '请选择房间状态' }]}>
            <Select>
               <Option value={0}>空闲</Option>
               <Option value={1}>不空闲</Option>
               <Option value={-1}>维护中</Option>
            </Select>
          </Form.Item>
          <Form.Item name="description" label="描述" rules={[{ required: true, message: '备注' }]}>
            <Input />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
};

export default RoomListPage;
