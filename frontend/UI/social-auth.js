const config = {
    google: {
        clientId: '571024471478-fq57e42cgt14oc4n283bu6hr42iag25a.apps.googleusercontent.com',
        redirectUri: 'http://127.0.0.1:5500/UI/auth/google/callback.html'
        //  redirectUri: 'http://127.0.0.1:5500/class%208/todoUi/todoUi/UI/login.html/'
    },
    facebook: {
        appId: 'YOUR_FACEBOOK_APP_ID',
        redirectUri: window.location.origin + '/auth/facebook/callback.html'
    }

}

function openPopup(url, width = 500, height = 600) {
    const left = (window.innerWidth - width) / 2;
    const top = (window.innerHeight - height) / 2;
    return window.open(url, 'Social Login', `width=${width}, height=${height}, left=${left}, top=${top}`);

}

function initGoogleSignIn() {
    const googleButton = document.querySelector('.social-button[title="Login with Google"]');
    if (googleButton) {
        googleButton.addEventListener('click', () => {
            const authURl = `https://accounts.google.com/o/oauth2/v2/auth?` +
                `client_id=${config.google.clientId}` +
                `&redirect_uri=${encodeURIComponent(config.google.redirectUri)}` +
                `&response_type=code` +
                `&scope=${encodeURIComponent('email profile')}` +
                `&access_type=offline` +
                `&prompt=consent`;

            const popup = openPopup(authURl);
            if (popup) {
                window.addEventListener('message', handleGoogleCallback);
            }
        })
    }
}

function initFacebookSignIn() {
    const facebookButton = document.querySelector('.social-button[title="Login with Facebook"]');
    if (facebookButton) {
        facebookButton.addEventListener('click', () => {
            const authURl = `https://www.facebook.com/v12.0/dialog/oauth?` +
                `client_id=${config.facebook.appId}` +
                `&redirect_uri=${encodeURIComponent(config.facebook.redirectUri)}` +
                `&scope=${encodeURIComponent('email public_profile')}` +
                `&response_type=code`;
            // `&access_type=offline` +
            // `&prompt=consent`;

            const popup = openPopup(authURl);
            if (popup) {
                window.addEventListener('message', handleFacebookCallback);
            }
        })
    }
}

function handleGoogleCallback(event) {
    // if (event.origin !== window.location.origin) {
    //     return;
    // }
    console.log(event.data)
    if (event.data.type === 'google-auth-success') {
        console.log("Testing log")
        const { code, userData } = event.data;
        handleSocialLoginSuccess('google', code, userData)
    }
}

function handleFacebookCallback(event) {
    if (event.origin !== window.location.origin) {
        return;
    }
    if (event.data.type === 'facebook-auth-success') {
        const { code, userData } = event.data;
        handleSocialLoginSuccess('facebook', code, userData)
    }
}

async function handleSocialLoginSuccess(provider, code, userData) {

    try {
        const loadingOverlay = document.getElementById('loading-overlay');
        if (loadingOverlay) {
            loadingOverlay.style.display = 'flex';
        }
        const requestData = {
            provider: provider,
            code: code,
            userData: {
                email: userData.email
            }
        }
        const response = await fetch('http://localhost:8080/api/social-auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestData)
        })
        if (!response.ok) {
            throw new Error('Failed to process Social Login');
        }

        const data = await response.json();

        if (data.user) {
            localStorage.setItem('userData', JSON.stringify({
                id: data.user.id,
                email: data.user.email
            }));
            window.location.href = '/UI/index.html';
        } else {
            throw new Error('No User Data received');
        }
    } catch (error) {
        console.log('Social Login error', error)
        showError('Failed to complete social login please try again.');
    } finally {
        if (loadingOverlay) {
            loadingOverlay.style.display = 'none';
        }
    }

}

function showError(message) {
    const errorElement = document.getElementById('error-message');
    if (errorElement) {
        errorElement.textContent = message;
        errorElement.style.display = 'block';
        setTimeout(() => {
            errorElement.style.display = 'none';
        }, 5000);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    initGoogleSignIn();
    initFacebookSignIn();
})
