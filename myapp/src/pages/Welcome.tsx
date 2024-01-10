import React from 'react';
import { PageContainer } from '@ant-design/pro-components';
import { Card, theme } from 'antd';

const Welcome: React.FC = () => {
  const { token } = theme.useToken();

  return (
    <PageContainer>
      <Card
        style={{
          borderRadius: 8,
          backgroundColor: token.colorBgContainer,
          boxShadow: token.boxShadow,
          display: 'flex', 
        }}
      >
        <div
          style={{
            flex: 1, 
            padding: '24px', 
          }}
        >
          <div>
            <div
              style={{
                fontSize: '24px',
                color: token.colorTextHeading,
                fontWeight: 'bold',
                marginBottom: '20px',
              }}
            >
              欢迎登录酒店管理系统
            </div>
            <p
              style={{
                fontSize: '16px',
                color: token.colorTextSecondary,
                lineHeight: '24px',
                width: '100%', 
              }}
            >
              酒店管理系统后端采用springBoot + Mybatis-Plus架构,前端使用antDesignPro脚手架快速开发，
              提供了便捷的酒店管理解决方案，我们致力于提供高效、易用的工具，帮助您管理酒店业务，提升运营效率。
            </p>
            <p
              style={{
                fontSize: '16px',
                color: token.colorTextSecondary,
                lineHeight: '24px',
                width: '100%', 
              }}
            >
              系统采用了角色分离，分为用户角色和管理员角色，为用户提供用户注册和房间预定功能，为管理员提供除用户功能以外的房间类型管理，房间管理，住宿管理，营收管理以及用户管理功能，
            </p>
          </div>
        </div>
        <div
          style={{
            flex: 0.35, 
            marginLeft: '20px', 
          }}
        >
          <img
            src="https://img.zcool.cn/community/0193285e747a2ba80120a895ca313c.jpg@1280w_1l_2o_100sh.jpg" 
            alt="Welcome Image"
            style={{ width: '100%', borderRadius: '8px' }}
          />
        </div>
      </Card>
    </PageContainer>
  );
};

export default Welcome;

