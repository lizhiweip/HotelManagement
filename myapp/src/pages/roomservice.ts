import request from 'umi-request';
const API_BASE_URL = 'http://localhost:8080';

export const saveRoom = async (roomData) => {
    const url = `${API_BASE_URL}/room/?roomTypeName=${roomData.roomTypeName}`;
    const options = {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(roomData),
    };
  
    const response = await fetch(url, options);
  
    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(`Error: ${response.status} - ${errorMessage}`);
    }
  
    return response.json();
  };
  



export const updateRoom = async (room) => {
    const url = `${API_BASE_URL}/room/update`;
    const options = {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(room),
    };

    const response = await fetch(url, options);

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Error: ${response.status} - ${errorMessage}`);
    }

    return response.json();
};


export const deleteRoom = async (roomId) => {
    const url = `${API_BASE_URL}/room/?roomId=${roomId}`;
    const options = {
        method: 'DELETE',
    };

    const response = await fetch(url, options);

    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Error: ${response.status} - ${errorMessage}`);
    }

    return response.json(); 
};


export const listRooms = async (params) => {
    const { roomNumber, status, roomTypeName } = params || {};
    let url = `${API_BASE_URL}/room/list`;
    
    
    if (roomNumber !== undefined && roomNumber !== null && roomNumber !== "") {
        console.log("房间号",roomNumber);
        
        url += `?roomNumber=${encodeURIComponent(roomNumber)}`;
    }

    if (status !== undefined && status !== null) {
        url += `${url.includes('?') ? '&' : '?'}status=${encodeURIComponent(status)}`;
    }

    if (roomTypeName !== undefined && status !== null) {
        url += `${url.includes('?') ? '&' : '?'}roomTypeName=${encodeURIComponent(roomTypeName)}`;
    }

    const response = await fetch(url);
    console.log("URL",url);
    if (!response.ok) {
        const errorMessage = await response.text();
        throw new Error(`Error: ${response.status} - ${errorMessage}`);
    }

    return response.json();
};

