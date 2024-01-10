import request from 'umi-request';

export interface RoomType {
    roomTypeId: string;
    roomTypeName: string;
    description: string;
    basePrice: number;  
    price: number;
  }
  
  export async function queryRoomTypeList(): Promise<{ data: RoomType[] }> {
    try {
      const response = await request('http://localhost:8080/roomType/list');
      return response;
    } catch (error) {
      throw new Error(`Error fetching data: ${error}`);
    }
  }
  
