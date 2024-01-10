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
  

  export async function saveRoomType(roomType) {
      const result= await request('http://localhost:8080/roomType', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(roomType),
      });
        return result.data; 
  }
  

  export async function deleteRoomType(roomTypeId: string): Promise<string> {
    
      const result = await request(`http://localhost:8080/roomType/?roomTypeId=${roomTypeId}`, {
        method: 'DELETE',
      });
       return result.data; 
  }

  const API_BASE_URL = 'http://localhost:8080';
  export const updateRoomType = async (roomType) => {
    // Convert RoomType to RoomTypeDto
    const roomTypeDto = {
      roomTypeId: roomType.roomTypeId,
      roomTypeName: roomType.roomTypeName,
      description: roomType.description,
      basePrice: roomType.basePrice,
      price: roomType.price, // Include the 'price' property
    };
  
    const url = `${API_BASE_URL}/roomType/update`;
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(roomTypeDto),
    };
  
    const response = await fetch(url, options);
  
    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(`Error: ${response.status} - ${errorMessage}`);
    }
  
    const result = await response.json();
    return result.data;
  };
  
  
  