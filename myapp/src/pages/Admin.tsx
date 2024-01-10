import React, { useEffect, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import { PlusOutlined } from '@ant-design/icons';
import ProTable from '@ant-design/pro-table';
import { Button, Modal, Form, Input, InputNumber, message, Popconfirm } from 'antd';
import { queryRoomTypeList, RoomType, saveRoomType, deleteRoomType, updateRoomType } from './service';

const RoomTypePage: React.FC = () => {
  const [data, setData] = useState<RoomType[]>([]);
  const [modalVisible, setModalVisible] = useState<boolean>(false);
  const [editingRecord, setEditingRecord] = useState<RoomType | null>(null);
  const [form] = Form.useForm();

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
    {
      title: '操作',
      dataIndex: 'operation',
      render: (_, record) => (
        <>
  <a onClick={() => handleEditRoomType(record)}>
    编辑
  </a>
  {' '} {/* Add a space here */}
  <Popconfirm
    title="确定删除该房间类型?"
    onConfirm={() => handleDeleteRoomType(record.roomTypeId)}
    okText="确定"
    cancelText="取消"
  >
    <a>删除</a>
  </Popconfirm>
</>
      ),
    },
  ];

  const handleAddRoomType = () => {
    setEditingRecord(null);
    setModalVisible(true);
  };

  const handleEditRoomType = (record: RoomType) => {
    setEditingRecord(record);
    form.setFieldsValue(record);
    setModalVisible(true);
  };

  const handleModalOk = async () => {
    try {
      const values = await form.validateFields();
      let result;
      if (editingRecord) {
        result = await updateRoomType({ ...editingRecord, ...values });
      } else {
        result = await saveRoomType(values);
      }

      message.success(result);
      setModalVisible(false);
      fetchData();
    } catch (error) {
      console.error('Error saving RoomType:', error);
      message.error('Failed to save RoomType. Please try again.');
    }
  };

  const handleModalCancel = () => {
    setEditingRecord(null);
    setModalVisible(false);
    form.resetFields();
  };

  const handleDeleteRoomType = async (roomTypeId: string) => {
    const result = await deleteRoomType(roomTypeId);
    message.success(result);
    fetchData();
  };

  return (
    <PageContainer>
      <ProTable
        columns={columns}
        dataSource={data}
        search={false}
        pagination={{ pageSize: 5 }}
        rowKey="roomTypeId"
        toolBarRender={() => [
          <Button key="addRoomType" icon={<PlusOutlined />} type="primary" onClick={handleAddRoomType}>
            添加房间类型
          </Button>,
        ]}
      />
  <Modal
  title="添加 RoomType"
  visible={modalVisible}
  onOk={handleModalOk}
  onCancel={handleModalCancel}
>
  <Form form={form} layout="vertical" name="addRoomTypeForm">
    <Form.Item
      name="roomTypeName"
      label="房间类型"
      rules={[{ required: true, message: '请输入房间类型' }]}
    >
      <Input />
    </Form.Item>
    <Form.Item name="description" label="描述">
      <Input />
    </Form.Item>
    <Form.Item
      name="basePrice"
      label="原价"
      rules={[{ required: true, message: '请输入原价' }]}
    >
      <InputNumber min={0} />
    </Form.Item>
    <Form.Item
  name="price"
  label="现价"
  rules={[{ required: true, message: '请输入现价' }]}
>
  <InputNumber min={0} />
</Form.Item>

  </Form>
</Modal>


    </PageContainer>
  );
};

export default RoomTypePage;