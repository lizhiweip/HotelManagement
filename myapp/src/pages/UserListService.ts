const API_BASE_URL = 'http://localhost:8080/user';

export const queryUserList = async (params) => {
  const url = `${API_BASE_URL}/list`;
  const queryString = new URLSearchParams(params).toString();

  try {
    const response = await fetch(`${url}?${queryString}`);
    
    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(`Error: ${response.status} - ${errorMessage}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error('Error querying user list:', error);
    throw error;
  }
};
