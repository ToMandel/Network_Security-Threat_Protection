import { Layout, Button, Space, Typography } from 'antd';
import { useNavigate } from "react-router-dom";

const { Header, Footer, Content } = Layout;
const { Title } = Typography;

const footerStyle = {
  textAlign: 'center',
  color: '#fff',
  backgroundColor: '#f4f2ee',
};
const headerStyle = {
  textAlign: 'center',
  color: '#fff',
  height: 64,
  paddingInline: 50,
  lineHeight: '64px',
  backgroundColor: '#f4f2ee',
};
const contentStyle = {
  textAlign: 'center',
  minHeight: 120,
  height:450,
  lineHeight: '120px',
  color: '#fff',
  backgroundColor: '#efe7d2',
};
function OpeningScreen (){
  const navigate = useNavigate();

  const Login = () => {
    navigate("/LoginPage");
  };
  const Register = () => {
    navigate("/RegisterPage");
  };
    return (
  
      <Space
      direction="vertical"
      style={{
        width: '100%',
      }}
      size={[0, 200]}
    >
      <Layout>
        <Header style={headerStyle}>
        <Title level={3} style={{color:'#22545c'}}>Comunication_LTD</Title>
  
        </Header>
        <Content style={contentStyle}>
      <Space wrap>
      <Button type="primary" onClick={Login}>
        Login
        </Button>
      <Button type="primary"  onClick={Register}>
        Register
        </Button>
      </Space>
        </Content>
        <Footer style={footerStyle}></Footer>
      </Layout>
      
      </Space>
      
    );
  }

  export default OpeningScreen;