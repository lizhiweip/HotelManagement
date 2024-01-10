import React from 'react';
import { Form, Input, Button, message } from 'antd';
import { recharge } from './service';

const UserRechargePage = () => {
  const [form] = Form.useForm();

  const handleRecharge = async () => {
    try {
      const { idCardNo, userName, amount } = await form.validateFields();
      console.log("idCardNo:", idCardNo);
      console.log("userName:", userName);
      console.log("amount:", amount);

      const response = await recharge(idCardNo, userName, amount);
      if (response.code === 1) {
        message.success(response.data);
        form.resetFields();
      } else {
        message.error(response.msg);
      }
    } catch (error) {
      console.error('Error recharging:', error);
      message.error('Failed to recharge. Please try again.');
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: 'auto', marginTop: 50 }}>
      <Form form={form} layout="vertical">
        <Form.Item
          name="idCardNo"
          label="身份证号"
          rules={[{ required: true, message: '请输入身份证号' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="userName"
          label="用户姓名"
          rules={[{ required: true, message: '请输入用户姓名' }]}
        >
          <Input />
        </Form.Item>
        <Form.Item
          name="amount"
          label="充值金额"
          rules={[{ required: true, message: '请输入充值金额' }]}
        >
          <Input type="number" min="0" step="10" />
        </Form.Item>
        <Form.Item>
          <Button type="primary" onClick={handleRecharge}>
            充值
          </Button>
        </Form.Item>
      </Form>
    </div>
  );
};

export default UserRechargePage;