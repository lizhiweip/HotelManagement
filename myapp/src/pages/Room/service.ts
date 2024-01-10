export interface RoomDto {
  roomId: string;
  roomNumber: string;
  status: number;
  description: string;
  roomTypeId: string;
  roomTypeName: string;
  price: number;
}
export const saveReservation = async (roomNum, checkInDate, checkOutDate, idCardNo) => {
  const apiUrl = `http://localhost:8080/reservation/?roomNum=${roomNum}&checkInDate=${checkInDate}&checkOutDate=${checkOutDate}&idCardNo=${idCardNo}`;
  console.log('URL', apiUrl);
  const response = await fetch(apiUrl, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
  });

  const result = await response.json();
  return result;
  console.log("result:" + result)
};



  
