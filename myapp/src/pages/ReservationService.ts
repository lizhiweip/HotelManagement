const API_BASE_URL = 'http://localhost:8080';

export const queryReservationList = async (roomNum, userName) => {
  let url = `${API_BASE_URL}/reservation/list`;
  if (roomNum || userName) {
    const queryParams = new URLSearchParams();
    if (roomNum) queryParams.append('roomNum', roomNum);
    if (userName) queryParams.append('userName', userName);
    url += `?${queryParams.toString()}`;
  }

  try {
    const response = await fetch(url);
    console.log(url);
    
    if (!response.ok) {
      throw new Error(`Error: ${response.status} - ${await response.text()}`);
    }

    const result = await response.json();
    console.log(result)
    return result;
  } catch (error) {
    console.error('Error querying reservation list:', error);
    throw error; 
  }
};
