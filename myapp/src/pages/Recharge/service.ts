const API_BASE_URL = 'http://localhost:8080/user';

export const recharge = async (idCardNo, userName, amount) => {
  const url = `${API_BASE_URL}/recharge`;
  const formData = new FormData();
  formData.append('idCardNo', idCardNo);
  formData.append('userName', userName);
  formData.append('count', amount);

  const options = {
    method: 'POST',
    body: formData,
  };

  try {
    const response = await fetch(url, options);

    if (!response.ok) {
      const errorMessage = await response.text();
      throw new Error(`Error: ${response.status} - ${errorMessage}`);
    }

    const result = await response.json();
    return result;
  } catch (error) {
    console.error('Error during recharge:', error);
    throw new Error('Failed to recharge. Please try again.');
  }
};
