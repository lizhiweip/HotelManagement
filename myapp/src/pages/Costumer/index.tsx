import React, { useState } from 'react';
import { Input, Button, InputNumber, Form,message } from 'antd';

import { saveUser } from './service'; 

const AddUserPage = () => {
  const [form] = Form.useForm();

  const handleSaveUser = async (values) => {
    try {
      await saveUser(values);
      message.success('注册成功');
      form.resetFields();
    } catch (error) {
      console.error('Error saving user:', error);
      message.error('注册失败，请重试');
    }
  };

  return (
    <Form
      form={form}
      onFinish={handleSaveUser}
      labelCol={{ span: 6 }}
      wrapperCol={{ span: 12 }}
    >
      <Form.Item name="userName" label="用户名" rules={[{ required: true, message: '请输入用户名' }]}>
  <Input />
</Form.Item>

<Form.Item name="idCardNo" label="身份证号" rules={[{ required: true, message: '请输入身份证号' }]}>
  <Input />
</Form.Item>



<Form.Item name="phoneNum" label="电话号码" rules={[{ required: true, message: '请输入电话号码' }]}>
  <Input />
</Form.Item>

<Form.Item name="balance" label="首次充值" rules={[{ required: true, message: '请输入账户余额' }]}>
  <InputNumber min={0} />
</Form.Item>
<Form.Item name="description" label="备注">
  <Input />
</Form.Item>
      <Form.Item wrapperCol={{ offset: 6, span: 12 }}>
        <Button type="primary" htmlType="submit">
          注册
        </Button>
      </Form.Item>
    </Form>
  );
};

export default AddUserPage;
