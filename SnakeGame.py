import pygame  # Imports the Pygame library, which is used for creating games in Python
import random  #Imports the random library, used for generating random numbers

# Initialize Pygame
pygame.init()  # Initializes all the Pygame modules, which is necessary before using Pygame

# Constants
SCREEN_WIDTH = 600  # Set the dimensions of the game window
SCREEN_HEIGHT = 600
TILE_SIZE = 25  # Defines the size of each tile in the grid
FPS = 10  #  Frames per second, which controls the game's speed

# Colors
BLACK = (0, 0, 0)
GREEN = (0, 255, 0)
RED = (255, 0, 0)


# Classes
class Tile:
    def __init__(self, x, y):  #  Initializes a tile with its x and y coordinates
        self.x = x
        self.y = y


class SnakeGame:
    def __init__(self):  #  Initializes the game state
        self.screen = pygame.display.set_mode((SCREEN_WIDTH, SCREEN_HEIGHT)) # Sets up the game window
        pygame.display.set_caption("Snake Game")  # Sets the window title
        self.clock = pygame.time.Clock()
        self.snake_head = Tile(5, 5)
        self.snake_body = [Tile(4, 5), Tile(3, 5)]
        self.food = Tile(10, 10)
        self.velocityX = 1  # Set the initial movement direction of the snake
        self.velocityY = 0
        self.game_over = False
        self.random = random
        self.place_food()

    def place_food(self):
        self.food.x = random.randint(0, SCREEN_WIDTH // TILE_SIZE - 1)
        self.food.y = random.randint(0, SCREEN_HEIGHT // TILE_SIZE - 1)

    def draw(self):
        self.screen.fill(BLACK)
        for part in self.snake_body:
            pygame.draw.rect(self.screen, GREEN, (part.x * TILE_SIZE, part.y * TILE_SIZE, TILE_SIZE, TILE_SIZE))
        pygame.draw.rect(self.screen, RED, (self.food.x * TILE_SIZE, self.food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE))
        pygame.display.update()

    def move(self):
        new_head = Tile(self.snake_head.x + self.velocityX, self.snake_head.y + self.velocityY)
        self.snake_body.insert(0, self.snake_head)
        self.snake_head = new_head

        if self.snake_head.x == self.food.x and self.snake_head.y == self.food.y:
            self.place_food()
        else:
            self.snake_body.pop()

        if self.snake_head.x < 0 or self.snake_head.x >= SCREEN_WIDTH // TILE_SIZE or self.snake_head.y < 0 or self.snake_head.y >= SCREEN_HEIGHT // TILE_SIZE:
            self.game_over = True

        for part in self.snake_body[1:]:
            if part.x == self.snake_head.x and part.y == self.snake_head.y:
                self.game_over = True

    def handle_events(self):
        for event in pygame.event.get():
            if event.type == pygame.QUIT:
                pygame.quit()
                quit()
            elif event.type == pygame.KEYDOWN:
                if event.key == pygame.K_UP and self.velocityY != 1:
                    self.velocityX = 0
                    self.velocityY = -1
                elif event.key == pygame.K_DOWN and self.velocityY != -1:
                    self.velocityX = 0
                    self.velocityY = 1
                elif event.key == pygame.K_LEFT and self.velocityX != 1:
                    self.velocityX = -1
                    self.velocityY = 0
                elif event.key == pygame.K_RIGHT and self.velocityX != -1:
                    self.velocityX = 1
                    self.velocityY = 0

    def run(self):
        while not self.game_over:
            self.handle_events()
            self.move()
            self.draw()
            self.clock.tick(FPS)

        pygame.quit()


# Main function
def main():
    game = SnakeGame()
    game.run()


if __name__ == "__main__":
    main()
