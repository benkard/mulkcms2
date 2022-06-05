document.addEventListener('DOMContentLoaded', () => {
  const messageEncoder = new TextEncoder();
  const hashcashAttemptBatchSize = 1000;

  const acceptableHash = (hash) => hash[0] === 0 && hash[1] === 0;

  const checkSalt = (message, salt, thenC, elseC) => {
    const saltedMessage = `Hashcash-Salt: ${salt}\n\n${message}`;
    const encodedSaltedMessage = messageEncoder.encode(saltedMessage);

    crypto.subtle.digest('SHA-256', encodedSaltedMessage).then((hashBuf) => {
      if (acceptableHash(new Uint8Array(hashBuf))) {
        thenC();
      } else {
        elseC();
      }
    });
  };

  const commentForms = document.getElementsByClassName('comment-form');
  for (const commentForm of commentForms) {
    const hashcashSaltInput = commentForm.querySelector('input[name=hashcash-salt]');
    const messageTextarea = commentForm.querySelector('textarea[name=message]');
    const submitButton = commentForm.querySelector('input[type=submit]');

    let salt = 0;
    const tryHashcash = () => {
      checkSalt(messageTextarea.value, salt, () => {
        hashcashSaltInput.value = salt;
        console.log(`hashcash ok ${salt}`);
        submitButton.toggleAttribute('disabled');
        commentForm.submit();
      }, () => {
        ++salt;
        if (salt % hashcashAttemptBatchSize === 0) {
          console.log(`hashcash fail ${salt}, retrying`);
        }
        setTimeout(tryHashcash, 0);
      });
    };

    commentForm.addEventListener(
        'submit',
        (event) => {
          event.preventDefault();
          submitButton.setAttribute('disabled', 'disabled');
          setTimeout(tryHashcash, 0);
        });
  }
});
