import request from 'umi-request';

export interface User {
  userName: string;
  idCardNo: string;
  description?: string;
  phoneNum: string;
  balance: number;
}

export async function saveUser(user: User): Promise<void> {
  
      await request('http://localhost:8080/user', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(user),
    });
}
