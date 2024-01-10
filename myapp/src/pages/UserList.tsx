import React, { useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import ProTable from '@ant-design/pro-table';
import { Button } from 'antd';
import { queryUserList } from './UserListService';

const UserListPage: React.FC = () => {
  const [searchParams, setSearchParams] = useState({});

  const columns = [
    {
      title: '用户名',
      dataIndex: 'userName',
    },
    {
      title: '身份证号',
      dataIndex: 'idCardNo',
    },
    {
      title: '手机号',
      dataIndex: 'phoneNum',
      search: false,
    },
    {
      title: '余额',
      dataIndex: 'balance',
      search: false,
    },
    {
      title: '描述',
      dataIndex: 'description',
      search: false,
    },
  ];

  return (
    <PageContainer>
      <ProTable
        columns={columns}
        request={async (params) => {
          setSearchParams(params);
          return queryUserList(params);
        }}
        search={{
          optionRender: ({ searchText, resetText }, { form }) => [
            <Button
              key="search"
              type="primary"
              onClick={() => {
                form?.submit();
              }}
            >
              {searchText}
            </Button>,
            <Button
              key="reset"
              onClick={() => {
                form?.resetFields();
                setSearchParams({});
              }}
            >
              {resetText}
            </Button>,
          ],
        }}
        rowKey="userId"
      />
    </PageContainer>
  );
};

export default UserListPage;
